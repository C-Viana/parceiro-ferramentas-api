package com.parceiroferramentas.api.parceiro_api.unit.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.parceiroferramentas.api.parceiro_api.config.DatabaseConfig;
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.CompradorRepository;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.SimurAuthenticationService;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.SimurPaymentService;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarrinhoRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = DatabaseConfig.getDatabaseConfig();

    @Autowired
    private CarrinhoRepository repository;
    @Autowired 
    private CompradorRepository compradorRepo;
    @Autowired
    private FerramentaRepository ferramentaRepo;

    @MockitoBean private SimurPaymentService simurPaymentService;
    @MockitoBean private SimurAuthenticationService simurAuthenticationService;

    static UUID usuarioId = UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f");

    @DynamicPropertySource
    static void configurePropertires(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        //registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        postgres.start();
    }

    @Test
    @Order(1)
    @DisplayName("Deve recuperar um item de carrinho por ID")
    //@Sql(scripts = "classpath:/db/migration/V11__inserir_itens_carrinho.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void encontrarItemDeCarrinhoPeloId() {
        ItemCarrinho response = repository.findById(3L).orElse(null);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getFerramenta());
    }

    @Test
    @Order(2)
    @DisplayName("Deve recuperar um carrinho inteiro pelo ID do usuário")
    void encontrarCarrinhoPeloIdUsuario() {
        List<ItemCarrinho> response = repository.findItemCarrinhoByCompradorId(usuarioId);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertNotNull(response.get(0).getFerramenta());
        Assertions.assertNotNull(response.get(1).getFerramenta());
    }

    @Test
    @Order(3)
    @DisplayName("Deve adicionar um novo item ao carrinho")
    void adicionarItemAoCarrinho() {
        Comprador comprador = compradorRepo.findById(usuarioId).orElse(null);
        Ferramenta ferramenta = ferramentaRepo.findById(5L).orElse(null);
        ItemCarrinho item = new ItemCarrinho();
        item.setComprador(comprador);
        item.setFerramenta(ferramenta);
        item.setQuantidade(1);
        item.setPrecoAluguelMomento(BigDecimal.valueOf(ferramenta.getPreco_aluguel()));
        item.setPrecoVendaMomento(BigDecimal.valueOf(ferramenta.getPreco_venda()));
        item.setUrlImage(ferramenta.getLista_imagens().get(0));

        ItemCarrinho response = repository.save(item);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(comprador, response.getComprador());
        Assertions.assertEquals(ferramenta, response.getFerramenta());

        List<ItemCarrinho> responseCarrinho = repository.findItemCarrinhoByCompradorId(usuarioId);
        Assertions.assertEquals(3, responseCarrinho.size());
    }

    @Test
    @Order(4)
    @DisplayName("Deve adicionar mais de um item ao carrinho")
    void adicionarMultiplosItensAoCarrinho() {
        UUID compradorId = UUID.fromString("0f0b3ab2-cacb-47d9-a00c-68e2340da9c9");
        Comprador comprador = compradorRepo.findById(compradorId).orElse(null);
        List<ItemCarrinho> itensParaAdicao = repository.findItemCarrinhoByCompradorId(compradorId);

        for (int i = 1; i <= 2; i++) {
            Ferramenta ferramenta = ferramentaRepo.findById((long)i).orElse(null);
            ItemCarrinho item = new ItemCarrinho();
            item.setComprador(comprador);
            item.setFerramenta(ferramenta);
            item.setQuantidade(1);
            item.setPrecoAluguelMomento(BigDecimal.valueOf(ferramenta.getPreco_aluguel()));
            item.setPrecoVendaMomento(BigDecimal.valueOf(ferramenta.getPreco_venda()));
            item.setUrlImage(ferramenta.getLista_imagens().get(0));
            itensParaAdicao.add(item);
        }

        List<ItemCarrinho> response = repository.saveAll(itensParaAdicao);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(itensParaAdicao.size(), response.size());

        List<ItemCarrinho> responseCarrinho = repository.findItemCarrinhoByCompradorId(compradorId);
        Assertions.assertEquals(itensParaAdicao.size(), responseCarrinho.size());
    }

    @Test
    @Order(5)
    @DisplayName("Deve atualizar um item do carrinho")
    void atualizarItemDoCarrinho() {
        ItemCarrinho itensParaAtualizar = repository.findItemCarrinhoByCompradorId(usuarioId).get(0);
        itensParaAtualizar.setQuantidade(3);

        repository.save(itensParaAtualizar);
        ItemCarrinho response = repository.findById(itensParaAtualizar.getId()).orElse(null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(3, response.getQuantidade());

        List<ItemCarrinho> responseCarrinho = repository.findItemCarrinhoByCompradorId(usuarioId);
        Optional<ItemCarrinho> itemAtualizado = responseCarrinho.stream()
            .filter(x -> x.getId() == itensParaAtualizar.getId())
            .findFirst();
        
        Assertions.assertEquals(3, itemAtualizado.get().getQuantidade());
    }

    @Test
    @Order(6)
    @DisplayName("Deve remover um item do carrinho")
    void removerItemDoCarrinho() {
        ItemCarrinho itensParaRemover = repository.findItemCarrinhoByCompradorId(usuarioId).get(0);
        repository.delete(itensParaRemover);
        ItemCarrinho response = repository.findById(itensParaRemover.getId()).orElse(null);

        Assertions.assertNull(response);
    }

    @Test
    @Order(7)
    @DisplayName("Deve remover todos os itens do carrinho")
    void removerTodosItensDoCarrinho() {
        List<ItemCarrinho> itensParaRemover = repository.findItemCarrinhoByCompradorId(usuarioId);
        repository.deleteAll(itensParaRemover);
        Assertions.assertTrue(repository.findItemCarrinhoByCompradorId(usuarioId).isEmpty());
    }

}
