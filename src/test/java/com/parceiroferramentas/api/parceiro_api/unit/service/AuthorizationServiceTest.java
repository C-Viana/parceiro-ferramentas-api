package com.parceiroferramentas.api.parceiro_api.unit.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.AcessoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PermissaoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Mock private AuthenticationManager authManager;
    @Mock private UsuarioRepository usuarioRepo;
    @Mock private PermissaoRepository permissoesRepo;
    @Mock private AcessoRepository acessoRepo;
    @Mock private JwtTokenService tokenService;
    @InjectMocks private UsuarioService usuarioService;

    private static AcessoUsuarioDto acessoDto;
    private static List<Usuario> mockedUsuarios;
    private static String senha = "senha1234";
    private static String senhaEncriptada = "$2a$10$AzCbojDDN5urYBKGcQk2Oewne3YiHMOOGsyndFejY2rpjldua2KzK";

    @BeforeAll
    static void setup() {
        mockedUsuarios = new ArrayList<>();
        mockedUsuarios.add(new Usuario(
            1L, "useradmin", "João Oliveira da Silva", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(1L, PerfisAcesso.ADMIN))
        ));
        mockedUsuarios.add(new Usuario(
            2L, "usergerente", "Cláudia Ferreira dos Santos", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(1L, PerfisAcesso.GERENTE))
        ));
        mockedUsuarios.add(new Usuario(
            3L, "uservendedor", "Luana Correia Costa", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(1L, PerfisAcesso.VENDEDOR))
        ));
        mockedUsuarios.add(new Usuario(
            4L, "usercliente", "Marcos Castro de Almeida", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(1L, PerfisAcesso.CLIENTE))
        ));
        mockedUsuarios.add(new Usuario(
            5L, "usuarioinexistente", "Usuario Não Cadastrado", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(1L, PerfisAcesso.CLIENTE))
        ));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        //mockRequest.setContextPath("/auth");
        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    @DisplayName("Deve logar com sucesso usando um usuário administrador")
    void loginComoAdmin_sucesso() {
        int userIndex = 0;
        acessoDto = new AcessoUsuarioDto(
            mockedUsuarios.get(userIndex).getUsername(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );
        Mockito.when(usuarioRepo.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername())).thenReturn(mockedUsuarios.get(userIndex));
        Mockito.when(tokenService.gerarAcesso(
            mockedUsuarios.get(userIndex).getUsername(), 
            mockedUsuarios.get(userIndex).getAuthorities().stream()
                .map(auth -> PerfisAcesso.valueOf(auth.getAuthority()))
                .toList()
        )).thenReturn(acessoDto);

        AcessoUsuarioDto resultado = usuarioService.signin(new CredenciaisUsuarioDto(mockedUsuarios.get(userIndex).getUsername(), senha));
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.username()).isEqualTo(mockedUsuarios.get(userIndex).getUsername());
    }

    @Test
    @DisplayName("Deve logar com sucesso usando um usuário gerente")
    void loginComoGerente_sucesso() {
        int userIndex = 1;
        acessoDto = new AcessoUsuarioDto(
            mockedUsuarios.get(userIndex).getUsername(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );
        Mockito.when(usuarioRepo.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername())).thenReturn(mockedUsuarios.get(userIndex));
        Mockito.when(tokenService.gerarAcesso(
            mockedUsuarios.get(userIndex).getUsername(), 
            mockedUsuarios.get(userIndex).getAuthorities().stream()
                .map(auth -> PerfisAcesso.valueOf(auth.getAuthority()))
                .toList()
        )).thenReturn(acessoDto);

        AcessoUsuarioDto resultado = usuarioService.signin(new CredenciaisUsuarioDto(mockedUsuarios.get(userIndex).getUsername(), senha));
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.username()).isEqualTo(mockedUsuarios.get(userIndex).getUsername());
    }

    @Test
    @DisplayName("Deve logar com sucesso usando um usuário vendedor")
    void loginComoVendedor_sucesso() {
        int userIndex = 2;
        acessoDto = new AcessoUsuarioDto(
            mockedUsuarios.get(userIndex).getUsername(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );
        Mockito.when(usuarioRepo.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername())).thenReturn(mockedUsuarios.get(userIndex));
        Mockito.when(tokenService.gerarAcesso(
                mockedUsuarios.get(userIndex).getUsername(), 
                mockedUsuarios.get(userIndex).getAuthorities().stream()
                    .map(auth -> PerfisAcesso.valueOf(auth.getAuthority()))
                    .toList()
        )).thenReturn(acessoDto);

        AcessoUsuarioDto resultado = usuarioService.signin(new CredenciaisUsuarioDto(mockedUsuarios.get(userIndex).getUsername(), senha));
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.username()).isEqualTo(mockedUsuarios.get(userIndex).getUsername());
    }

    @Test
    @DisplayName("Deve logar com sucesso usando um usuário cliente")
    void loginComoCliente_sucesso() {
        int userIndex = 3;
        acessoDto = new AcessoUsuarioDto(
            mockedUsuarios.get(userIndex).getUsername(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );
        Mockito.when(usuarioRepo.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername())).thenReturn(mockedUsuarios.get(userIndex));
        Mockito.when(tokenService.gerarAcesso(
                mockedUsuarios.get(userIndex).getUsername(), 
                mockedUsuarios.get(userIndex).getAuthorities().stream()
                    .map(auth -> PerfisAcesso.valueOf(auth.getAuthority()))
                    .toList()
        )).thenReturn(acessoDto);

        AcessoUsuarioDto resultado = usuarioService.signin(new CredenciaisUsuarioDto(mockedUsuarios.get(userIndex).getUsername(), senha));
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.username()).isEqualTo(mockedUsuarios.get(userIndex).getUsername());
    }

    @Test
    @DisplayName("Deve lançar a exceção UsernameNotFoundException ao logar com um usuário inexistente")
    void usernameNotFoundException_usuarioInexistente_excecao() {
        int userIndex = 4;
        acessoDto = new AcessoUsuarioDto(
            "nomeusuarioinexistente",
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );
        Mockito.when(usuarioRepo.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername())).thenThrow(new UsernameNotFoundException("Usuário ["+mockedUsuarios.get(userIndex).getUsername()+"] não encontrado"));

        Assertions.assertThatExceptionOfType(
            UsernameNotFoundException.class).isThrownBy(() -> {
                usuarioService.signin(new CredenciaisUsuarioDto(mockedUsuarios.get(userIndex).getUsername(), senha));
            })
            .withMessageContaining("Usuário ["+mockedUsuarios.get(userIndex).getUsername()+"] não encontrado");
    }

    @Test
    @DisplayName("Deve lançar a exceção InvalidAuthorizationException ao logar com um usuário com senha inválida")
    void invalidAuthorizationException_senhaIncorreta_excecao() {
        int userIndex = 1;
        acessoDto = new AcessoUsuarioDto(
            mockedUsuarios.get(userIndex).getUsername(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(3),
            "SENHA_INVALIDA.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNjIyOTAsInN1YiI6IjgwNjkwNTcxIn0.GonnWko3af3wm1u5wFomBHe7OqZB0WUOySxVlLBN77Q"
        );

        Mockito.when(usuarioRepo.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername())).thenReturn(mockedUsuarios.get(userIndex));
        Mockito.doThrow(new RuntimeException("Bad credentials")).when(authManager).authenticate(Mockito.any());

        Assertions.assertThatExceptionOfType(
            InvalidAuthorizationException.class).isThrownBy(() -> {
                usuarioService.signin(new CredenciaisUsuarioDto(mockedUsuarios.get(userIndex).getUsername(), senha));
            })
            .withMessageContaining("O acesso não foi permitido. Verifique se os dados estão corretos ou tente novamente");
    }
}
