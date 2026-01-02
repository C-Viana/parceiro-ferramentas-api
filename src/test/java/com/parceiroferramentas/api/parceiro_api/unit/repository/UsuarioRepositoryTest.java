package com.parceiroferramentas.api.parceiro_api.unit.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.parceiroferramentas.api.parceiro_api.config.DatabaseConfig;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.AcessoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PermissaoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class UsuarioRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = DatabaseConfig.getDatabaseConfig();

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AcessoRepository acessoRepository;

    @Autowired
    PermissaoRepository permissaoRepository;

    private static List<Usuario> mockedUsuarios;

    @DynamicPropertySource
    static void configurePropertires(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    private void setPermission() {
        permissaoRepository.saveAll(CreateMockedData.getInstance().getPermissoes());
    }

    @BeforeAll
    static void setup() {
        mockedUsuarios = CreateMockedData.getInstance().getUsuarios();
    }

    // @Test
    // @Order(1)
    // @DisplayName("Deve salvar um conjunto de permissões no repositório")
    public void deveSalvarPermissoes() {
        int userIndex = 0;
        
        List<Permissao> response = permissaoRepository.saveAll(CreateMockedData.getInstance().getPermissoes());
        
        Assertions.assertThatObject(response).isNotNull();
        Assertions.assertThatObject(response.get(userIndex).getId()).isEqualTo(1L);
        Assertions.assertThatObject(PerfisAcesso.valueOf(response.get(userIndex).getAuthority())).isEqualTo(PerfisAcesso.ADMIN);
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar um novo usuário no repositório")
    public void deveSalvarUsuario() {
        int userIndex = 0;
        setPermission();
        Usuario resUsuario = usuarioRepository.save(mockedUsuarios.get(userIndex));
        Assertions.assertThatObject(resUsuario).isNotNull();
        Assertions.assertThat(resUsuario.getId()).isGreaterThan(0);
        Assertions.assertThat(resUsuario.getNome()).isEqualTo(mockedUsuarios.get(userIndex).getNome());
    }

    @Test
    @Order(2)
    @DisplayName("Deve recuperar um usuário pelo username do repositório")
    public void deveRetornarUsuarioPorNomeDeUsuario() {
        int userIndex = 1;
        setPermission();
        usuarioRepository.save(mockedUsuarios.get(userIndex));
        Usuario resUsuario = usuarioRepository.findUsuarioByUsername(mockedUsuarios.get(userIndex).getUsername());
        Assertions.assertThatObject(resUsuario).isNotNull();
        Assertions.assertThat(resUsuario.getId()).isGreaterThan(0);
        Assertions.assertThat(resUsuario.getNome()).isEqualTo(mockedUsuarios.get(userIndex).getNome());
    }

    @Test
    @Order(3)
    @DisplayName("Deve recuperar lista de usuários de acordo com o perfil")
    public void deveRetornarUsuariosFiltradosPorPerfil() {
        int userIndex = 2;
        setPermission();
        Permissao perfil = permissaoRepository.findPermissaoByAuthority(PerfisAcesso.VENDEDOR);
        
        System.out.println( "PERMISSAO: " + perfil );
        Usuario resUsuario = mockedUsuarios.get(userIndex);
        resUsuario.setAuthorities(List.of(perfil));
        usuarioRepository.save(resUsuario);
        
        Page<Usuario> response = usuarioRepository.findByAuthoritiesContains(perfil, PageRequest.of(0, 12, Sort.by(Direction.ASC, "id")));

        Assertions.assertThatObject(response).isNotNull();
        Assertions.assertThatObject(response.getContent().size()).isEqualTo(1);
        Assertions.assertThat(response.getContent().get(0).getId()).isGreaterThan(0);
        Assertions.assertThat(response.getContent().get(0).getNome()).isEqualTo(mockedUsuarios.get(userIndex).getNome());
    }

    
}
