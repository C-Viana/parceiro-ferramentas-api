package com.parceiroferramentas.api.parceiro_api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;
import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;
import com.parceiroferramentas.api.parceiro_api.model.Acesso;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.AcessoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PermissaoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PermissaoRepository permissaoRepo;

    @Autowired
    private AcessoRepository acessoRepo;

    public UsuarioService(@Lazy AuthenticationManager authManager, JwtTokenService tokenService,
            UsuarioRepository usuarioRepo, PermissaoRepository permissaoRepo, AcessoRepository acessoRepo) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.usuarioRepo = usuarioRepo;
        this.permissaoRepo = permissaoRepo;
        this.acessoRepo = acessoRepo;
    }

    public AcessoUsuarioDto signin(CredenciaisUsuarioDto credenciais) {
        Usuario user = usuarioRepo.findUsuarioByUsername(credenciais.username());
        if(user == null) throw new UsernameNotFoundException("Usuário ["+credenciais.username()+"] não encontrado");

        AcessoUsuarioDto acesso = tokenService.gerarAcesso(
            credenciais.username(), 
            user.getAuthorities()
                .stream()
                .map(auth -> PerfisAcesso.valueOf(auth.getAuthority()))
                .toList()
        );

        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credenciais.username(), credenciais.senha())
            );
        } catch (Exception e) {
            throw new InvalidAuthorizationException("O acesso não foi permitido. Verifique se os dados estão corretos ou tente novamente");
        }
        
        return acesso;
    }

    public AcessoUsuarioDto refresh(String nomeUsuario, String refreshToken) {
        if(nomeUsuario == null || nomeUsuario.isBlank() ) throw new InvalidAuthorizationException("Nome de usuário inválido ou ausente");
        if(refreshToken == null || refreshToken.isBlank() ) throw new InvalidAuthorizationException("O token fornecido está inválido ou ausente");
        Usuario usuario = usuarioRepo.findUsuarioByUsername(nomeUsuario);
        if(usuario == null) throw new UsernameNotFoundException("Usuário ["+nomeUsuario+"] não encontrado");

        return tokenService.renovarAcesso(refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findUsuarioByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("O usuário ["+username+"] não foi encontrado");
        }
        return usuario;
    }

    public Usuario signup(Usuario usuario) {
        List<Permissao> perms = null ;
        
        if(usuario == null || 
            usuario.getPassword() == null || usuario.getPassword().isBlank() || 
            usuario.getUsername() == null || usuario.getUsername().isBlank() || 
            usuario.getNome() == null || usuario.getNome().isBlank())
                throw new BadRequestException("Informações incompletas. Certifique-se que os campos USUÁRIO, NOME e SENHA foram informados");
        
        if(usuarioRepo.findUsuarioByUsername(usuario.getUsername()) != null)
            throw new BadRequestException("Esse nome de usuário já existe. É necessário usar um nome diferente");
        
        usuario.setAccount_non_expired(true);
        usuario.setAccount_non_locked(true);
        usuario.setCredentials_non_expired(true);
        usuario.setEnabled(true);
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));

        if(usuario.getAuthorities() == null || usuario.getAuthorities().isEmpty()) {
            logger.info("Nenhum acesso foi informado para o novo usuário. Definindo acesso padrão de CLIENTE");
            Permissao p = permissaoRepo.findPermissaoByAuthority(PerfisAcesso.CLIENTE);
            usuario.setAuthorities(List.of(p));
        }
        else {
            perms = usuario.getAuthorities().stream().map(role -> {
                Permissao p = permissaoRepo.findPermissaoByAuthority(PerfisAcesso.valueOf(role.getAuthority()));
                if(p == null)
                    throw new BadRequestException("O perfil de acesso ["+role.getAuthority()+"] é inválido ou não existe");
                return p;
            }).toList();
            usuario.setAuthorities(perms);
        }
        
        Usuario novoUsuario = usuarioRepo.save(usuario);

        try {
            if(perms != null) perms.forEach(p -> acessoRepo.save(new Acesso(novoUsuario, p)));
            return novoUsuario;
        } catch (Exception e) {
            usuarioRepo.delete(novoUsuario);
            logger.error("Erro ao cadastrar novo usuário: {}", e.getMessage());
            throw e;
        }

    }

    public Page<Usuario> findAllUsuarios(String token, Pageable pageable) {
        if(tokenService.tokenExpirado(token))
            throw new InvalidAuthorizationException("O token fornecido está expirado ou inválido");
        return usuarioRepo.findAll(pageable);
    }

    public Page<Usuario> findByAuthoritiesContains(String token, String nomePermissao, Pageable pageable) {
        Permissao p = permissaoRepo.findPermissaoByAuthority(PerfisAcesso.valueOf(nomePermissao.toUpperCase()));
        if(p == null)
            throw new BadRequestException("O perfil de acesso informado é inválido ou não existe");
        if(tokenService.tokenExpirado(token))
            throw new InvalidAuthorizationException("O token fornecido está expirado ou inválido");
        return usuarioRepo.findByAuthoritiesContains(p, pageable);
    }

}
