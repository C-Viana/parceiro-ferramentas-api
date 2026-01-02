package com.parceiroferramentas.api.parceiro_api.unit.repository;

import java.util.Comparator;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.parceiroferramentas.api.parceiro_api.config.DatabaseConfig;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FerramentaRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = DatabaseConfig.getDatabaseConfig();

    @Autowired
    private FerramentaRepository repository;

    @DynamicPropertySource
    static void configurePropertires(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar novas ferramentas no repositório")
    void deveSalvarNovasFerramentas() {
        List<Ferramenta> ferramenta = CreateMockedData.getInstance().getFerramentas();
        ferramenta.stream().sorted(Comparator.comparing(x -> x.getId())).forEach(item -> {
            item.setId(null);
            repository.save(item);
        });

        Page<Ferramenta> resultado = repository.findAll(PageRequest.of(0, 50));
        Assertions.assertThat(resultado.getTotalElements()).isEqualTo(ferramenta.size());
        Assertions.assertThatObject(resultado.getContent().get(0).equals(ferramenta.get(0)));
    }

    @Test
    @Order(2)
    @DisplayName("Deve realizar busca bem sucedida pelo tipo de ferramenta ignorando maíusculas e minúsculas")
    void deveEncontrarPorTipoIgnoreCase() {
        String expectedTipe = "TIPOtestE";

        Ferramenta ferramenta = CreateMockedData.getInstance().getNovaFerramenta();
        ferramenta.setId(null);
        ferramenta.setTipo(expectedTipe);
        repository.save(ferramenta);

        Page<Ferramenta> resultado = repository.findByTipoEqualsIgnoreCase("tipoteste", PageRequest.of(0, 10));
        Assertions.assertThat(resultado).hasSize(1);
        Assertions.assertThat(resultado.getContent().get(0).getTipo()).isEqualTo(expectedTipe);
    }

    @Test
    @Order(3)
    @DisplayName("Deve alterar com sucesso os dados de uma ferramenta já cadastrada")
    void deveAlterarDadosDeFerramentaCadastrada() {
        Ferramenta ferramenta = CreateMockedData.getInstance().getNovaFerramenta();
        ferramenta.setId(null);
        Long ID = repository.save(ferramenta).getId();

        Ferramenta resultadoOriginal = repository.findById(ID).orElse(null);
        ferramenta.setCriado_em(resultadoOriginal.getCriado_em());
        ferramenta.setId(resultadoOriginal.getId());
        resultadoOriginal.setDescricao("Descrição atualizada");
        resultadoOriginal.setPreco_venda(235D);
        
        repository.save(resultadoOriginal);
        Ferramenta resultadoAtualizado = repository.findById(ID).orElse(null);

        Assertions.assertThat(resultadoAtualizado).isNotNull();
        Assertions.assertThat(resultadoOriginal.getId()).isEqualTo(ferramenta.getId());
        Assertions.assertThat(resultadoOriginal.getCriado_em()).isEqualTo(ferramenta.getCriado_em());
        Assertions.assertThat(resultadoOriginal.getDescricao()).isEqualTo("Descrição atualizada");
        Assertions.assertThat(resultadoOriginal.getPreco_venda()).isNotEqualTo(ferramenta.getPreco_venda());
        Assertions.assertThat(resultadoOriginal.getPreco_venda()).isEqualTo(235D);
    }

    @Test
    @Order(4)
    @DisplayName("Deve remover com sucesso o registro de uma ferramenta")
    void deveRemoverFerramentaCadastrada() {
        Ferramenta ferramenta = CreateMockedData.getInstance().getNovaFerramenta();
        ferramenta.setId(null);
        Long ID = repository.save(ferramenta).getId();
        repository.deleteById(ID);

        Ferramenta resultadoAtualizado = repository.findById(ID).orElse(null);

        Assertions.assertThat(resultadoAtualizado).isNull();
    }

}
