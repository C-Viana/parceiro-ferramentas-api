package com.parceiroferramentas.api.parceiro_api.unit.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AuthenticationManager;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.AcessoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PermissaoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock private AuthenticationManager authManager;
    @Mock private UsuarioRepository usuarioRepo;
    @Mock private PermissaoRepository permissoesRepo;
    @Mock private AcessoRepository acessoRepo;
    @Mock private JwtTokenService tokenService;
    @Mock private DecodedJWT decodedJWT;
    @Mock private Claim claim;
    @InjectMocks private UsuarioService usuarioService;

    private static AcessoUsuarioDto acessoDto;
    private static List<Usuario> mockedUsuarios;
    private static PageRequest pageRequest;
    //private static final String MSG_SEM_PERMISSAO = "Acesso negado. Permissão de ADMINISTRADOR é necessária para essa operação";

    @BeforeAll
    static void setup() {
        mockedUsuarios = CreateMockedData.getInstance().getUsuarios();
        pageRequest = PageRequest.of(0, 12, Sort.by(Direction.ASC, "id"));
    }

    private AcessoUsuarioDto gerarAcesso(int userIndex) {
        return new AcessoUsuarioDto(
            mockedUsuarios.get(userIndex).getUsername(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );
    }

    @Test
    @DisplayName("Deve retornar lista de usuários com sucesso quando utilizar perfil de administrador")
    void buscarUsuariosComAdministrador() {
        int userIndex = 0;
        acessoDto = gerarAcesso(userIndex);

        Page<Usuario> pagina = new PageImpl<>(mockedUsuarios, pageRequest, mockedUsuarios.size());
        
        //Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        //Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        //Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        //Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.ADMIN.getString()));
        Mockito.when(usuarioRepo.findAll(pageRequest)).thenReturn(pagina);

        Page<Usuario> resultado = usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest);
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getTotalElements()).isEqualTo(pagina.getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar lista de usuários com sucesso quando utilizar perfil de gerente")
    void buscarUsuariosComGerente() {
        int userIndex = 1;
        acessoDto = gerarAcesso(userIndex);

        Page<Usuario> pagina = new PageImpl<>(mockedUsuarios, pageRequest, mockedUsuarios.size());
        
        // Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        // Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        // Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        // Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.GERENTE.getString()));
        Mockito.when(usuarioRepo.findAll(pageRequest)).thenReturn(pagina);

        Page<Usuario> resultado = usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest);
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getTotalElements()).isEqualTo(pagina.getTotalElements());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuários com perfil de vendedor")
    void buscarUsuariosComVendedor_excessao() {
        int userIndex = 3;
        acessoDto = gerarAcesso(userIndex);

        Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        //Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        //Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        //Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.VENDEDOR.getString()));

        Assertions.assertThat(usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest)).isNull();
        // Assertions.assertThatExceptionOfType(InvalidAuthorizationException.class)
        //     .isThrownBy(() -> {
        //         usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest);
        //     })
        //     .withMessage(MSG_SEM_PERMISSAO);
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuários com perfil de cliente")
    void buscarUsuariosComCliente_excessao() {
        int userIndex = 4;
        acessoDto = gerarAcesso(userIndex);

        Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        // Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        // Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        // Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.VENDEDOR.getString()));

        Assertions.assertThat(usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest)).isNull();

        // Assertions.assertThatExceptionOfType(InvalidAuthorizationException.class)
        //     .isThrownBy(() -> {
        //         usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest);
        //     })
        //     .withMessage(MSG_SEM_PERMISSAO);
    }

    @Test
    @DisplayName("Deve retornar uma lista filtrada ao buscar usuários com perfil de administrador")
    void buscarUsuariosFiltradosComAdmin() {
        int userIndex = 0;
        acessoDto = gerarAcesso(userIndex);

        Page<Usuario> pagina = new PageImpl<>(Arrays.asList(mockedUsuarios.get(1)), pageRequest, mockedUsuarios.size());
        
        Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        //Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        //Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        //Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.ADMIN.getString()));
        Mockito.when(permissoesRepo.findPermissaoByAuthority(PerfisAcesso.GERENTE)).thenReturn(CreateMockedData.getInstance().getPermissoes().get(1));
        Mockito.when(usuarioRepo.findByAuthoritiesContains(CreateMockedData.getInstance().getPermissoes().get(1), pageRequest)).thenReturn(pagina);

        Page<Usuario> resultado = usuarioService.findByAuthoritiesContains(acessoDto.acesso(), PerfisAcesso.GERENTE.getString(), pageRequest);
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(resultado.getContent().get(0).getUsername()).isEqualTo("usergerente");
    }

    @Test
    @DisplayName("Deve retornar uma lista filtrada ao buscar usuários com perfil de gerente")
    void buscarUsuariosFiltradosComGerente() {
        int userIndex = 1;
        acessoDto = gerarAcesso(userIndex);

        Page<Usuario> pagina = new PageImpl<>(Arrays.asList(mockedUsuarios.get(0)), pageRequest, mockedUsuarios.size());
        
        Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        // Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        // Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        // Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.GERENTE.getString()));
        Mockito.when(permissoesRepo.findPermissaoByAuthority(PerfisAcesso.ADMIN)).thenReturn(CreateMockedData.getInstance().getPermissoes().get(0));
        Mockito.when(usuarioRepo.findByAuthoritiesContains(CreateMockedData.getInstance().getPermissoes().get(0), pageRequest)).thenReturn(pagina);

        Page<Usuario> resultado = usuarioService.findByAuthoritiesContains(acessoDto.acesso(), PerfisAcesso.ADMIN.getString(), pageRequest);
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(resultado.getContent().get(0).getUsername()).isEqualTo("useradmin");
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuários utilizando filtro com perfil de vendedor")
    void buscarUsuariosFiltradosComVendedor_excessao() {
        int userIndex = 2;
        acessoDto = gerarAcesso(userIndex);

        // Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.VENDEDOR.getString()));
        // Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        // Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        // Mockito.when(permissoesRepo.findPermissaoByAuthority(PerfisAcesso.VENDEDOR)).thenReturn(CreateMockedData.getInstance().getPermissoes().get(2));
        Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);

        Assertions.assertThat(usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest)).isNull();

        // Assertions.assertThatExceptionOfType(InvalidAuthorizationException.class)
        //     .isThrownBy(() -> {
        //         usuarioService.findByAuthoritiesContains(acessoDto.acesso(), PerfisAcesso.VENDEDOR.getString(), pageRequest);
        //     })
        //     .withMessage(MSG_SEM_PERMISSAO);
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuários utilizando filtro com perfil de cliente")
    void buscarUsuariosFiltradosComCliente_excessao() {
        int userIndex = 2;
        acessoDto = gerarAcesso(userIndex);

        Mockito.when(tokenService.tokenExpirado(acessoDto.acesso())).thenReturn(false);
        // Mockito.when(tokenService.decodeToken(acessoDto.acesso())).thenReturn(decodedJWT);
        // Mockito.when(decodedJWT.getClaim("roles")).thenReturn(claim);
        // Mockito.when(claim.asList(String.class)).thenReturn(Arrays.asList(PerfisAcesso.CLIENTE.getString()));
        // Mockito.when(permissoesRepo.findPermissaoByAuthority(PerfisAcesso.CLIENTE)).thenReturn(CreateMockedData.getInstance().getPermissoes().get(3));

        Assertions.assertThat(usuarioService.findAllUsuarios(acessoDto.acesso(), pageRequest)).isNull();

        // Assertions.assertThatExceptionOfType(InvalidAuthorizationException.class)
        //     .isThrownBy(() -> {
        //         usuarioService.findByAuthoritiesContains(acessoDto.acesso(), PerfisAcesso.CLIENTE.getString(), pageRequest);
        //     })
        //     .withMessage(MSG_SEM_PERMISSAO);
    }

}
