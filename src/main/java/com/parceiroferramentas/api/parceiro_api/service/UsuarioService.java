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
import org.springframework.transaction.annotation.Transactional;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.enums.PERFIL_ACESSO;
import com.parceiroferramentas.api.parceiro_api.exception.NotFoundException;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.PermissaoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsuarioService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PermissaoRepository permissaoRepo;

    public UsuarioService(@Lazy AuthenticationManager authManager, JwtTokenService tokenService,
            UsuarioRepository usuarioRepo, PermissaoRepository permissaoRepo) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.usuarioRepo = usuarioRepo;
        this.permissaoRepo = permissaoRepo;
    }

    public AcessoUsuarioDto signin(CredenciaisUsuarioDto credenciais) {
        Usuario user = usuarioRepo.findUsuarioByUsername(credenciais.username());
        if(user == null) throw new UsernameNotFoundException("Usuário ["+credenciais.username()+"] não encontrado");

        AcessoUsuarioDto acesso = tokenService.gerarAcesso(
            credenciais.username(), 
            user.getAuthorities()
                .stream()
                .map(auth -> PERFIL_ACESSO.valueOf(auth.getAuthority()))
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

    public Usuario fallbackSignup(Usuario usuario, Throwable throwable) {
        logger.error("CIRCUIT BREAKER: erro ao criar novo usuário", throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackSignup")
    @RateLimiter(name = "authRateLimit", fallbackMethod = "fallbackSignup")
    public Usuario signup(Usuario usuario) {
        List<Permissao> perms = null ;
        
        if(usuarioRepo.findUsuarioByUsername(usuario.getUsername()) != null)
            throw new NotFoundException("Esse nome de usuário já existe. É necessário usar um nome diferente");
        
        usuario.setAccount_non_expired(true);
        usuario.setAccount_non_locked(true);
        usuario.setCredentials_non_expired(true);
        usuario.setEnabled(true);
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));

        if(usuario.getAuthorities() == null || usuario.getAuthorities().isEmpty()) {
            logger.info("Nenhum acesso foi informado para o novo usuário. Definindo acesso padrão de CLIENTE");
            Permissao p = permissaoRepo.findPermissaoByAuthority(PERFIL_ACESSO.CLIENTE);
            usuario.setAuthorities(List.of(p));
        }
        else {
            perms = usuario.getAuthorities().stream().map(role -> {
                Permissao p = permissaoRepo.findPermissaoByAuthority(PERFIL_ACESSO.valueOf(role.getAuthority()));
                if(p == null)
                    throw new NotFoundException("O perfil de acesso ["+role.getAuthority()+"] é inválido ou não existe");
                return p;
            }).toList();
            usuario.setAuthorities(perms);
        }
        
        Usuario novoUsuario = usuarioRepo.save(usuario);
        return novoUsuario;

    }

    public Page<Usuario> findAllUsuarios(String token, Pageable pageable) {
        if(tokenService.tokenExpirado(token))
            throw new InvalidAuthorizationException("O token fornecido está expirado ou inválido");
        return usuarioRepo.findAll(pageable);
    }

    public Page<Usuario> findByAuthoritiesContains(String token, String nomePermissao, Pageable pageable) {
        if(tokenService.tokenExpirado(token))
            throw new InvalidAuthorizationException("O token fornecido está expirado ou inválido");

        Permissao p = permissaoRepo.findPermissaoByAuthority(PERFIL_ACESSO.valueOf(nomePermissao.toUpperCase()));
        
        if(p == null)
            throw new NotFoundException("O perfil de acesso informado é inválido ou não existe");
        return usuarioRepo.findByAuthoritiesContains(p, pageable);
    }

}
