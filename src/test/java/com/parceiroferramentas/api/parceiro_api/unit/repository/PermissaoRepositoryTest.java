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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.parceiroferramentas.api.parceiro_api.config.DatabaseConfig;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.enums.PerfilAcesso;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.repository.AcessoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PermissaoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.SimurAuthenticationService;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.SimurPaymentService;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class PermissaoRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = DatabaseConfig.getDatabaseConfig();

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AcessoRepository acessoRepository;

    @Autowired
    PermissaoRepository permissaoRepository;

    @MockitoBean private SimurPaymentService simurPaymentService;
    @MockitoBean private SimurAuthenticationService simurAuthenticationService;

    private static List<Permissao> permissoesMock;

    @DynamicPropertySource
    static void configurePropertires(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @BeforeAll
    static void setup() {
        permissoesMock = CreateMockedData.getInstance().getPermissoes();
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar um conjunto de permissões no repositório")
    public void deveSalvarPermissoes() {
        int userIndex = 0;
        List<Permissao> response = permissaoRepository.saveAll(CreateMockedData.getInstance().getPermissoes());
        
        Assertions.assertThatObject(response).isNotNull();
        Assertions.assertThatObject(response.get(userIndex).getId()).isEqualTo(1L);
        Assertions.assertThatObject(response.get(userIndex).getAuthority()).isEqualTo(PerfilAcesso.ADMIN.toString());
    }

    @Test
    @Order(2)
    @DisplayName("Deve recuperar uma permissão no repositório")
    public void deveRecuperarPermissao() {
        int userIndex = 0;

        permissaoRepository.save(permissoesMock.get(userIndex));
        Permissao response = permissaoRepository.findPermissaoByAuthority(PerfilAcesso.valueOf(permissoesMock.get(userIndex).getAuthority()));
        
        Assertions.assertThatObject(response).isNotNull();
        Assertions.assertThatObject(response.getId()).isEqualTo(permissoesMock.get(userIndex).getId());
        Assertions.assertThatObject(response.getAuthority()).isEqualTo(permissoesMock.get(userIndex).getAuthority());
    }
    
}
