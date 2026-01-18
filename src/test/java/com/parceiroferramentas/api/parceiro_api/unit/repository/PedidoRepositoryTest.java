package com.parceiroferramentas.api.parceiro_api.unit.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.parceiroferramentas.api.parceiro_api.config.DatabaseConfig;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.model.pedido.ItemPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.STATUS_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TIPO_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.repository.EnderecoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PedidoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PedidoRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = DatabaseConfig.getDatabaseConfig();

    @Autowired
    private PedidoRepository repository;

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private EnderecoRepository enderecoRepository;
    @Autowired private FerramentaRepository ferramentaRepo;

    @DynamicPropertySource
    static void configurePropertires(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Test
    @DisplayName("Buscar pedido pelo ID")
    @Order(1)
    void buscarPedidoTest() {
        Pedido pedido = repository.findById(1L).orElse(null);
        Assertions.assertNotNull(pedido);
        Assertions.assertNotNull(pedido.getId());
        Assertions.assertNotNull(pedido.getValorTotal());
        Assertions.assertNotNull(pedido.getTipo());
        Assertions.assertNotNull(pedido.getSituacao());
        Assertions.assertNotNull(pedido.getDataCriacao());
        Assertions.assertNotNull(pedido.getDataAtualizacao());
        Assertions.assertNotNull(pedido.getDataFim());
    }

    @Test
    @DisplayName("Buscar pedidos do usuário")
    @Order(2)
    void buscarPedidosDoUsuarioTest() {
        long usuarioId = 4L;
        List<Pedido> pedidos = repository.findPedidoByUsuarioId(usuarioId);
        Assertions.assertNotNull(pedidos);
        Assertions.assertEquals(2, pedidos.size());
        Assertions.assertNotNull(pedidos.get(0).getId());
        Assertions.assertNotNull(pedidos.get(0).getValorTotal());
        Assertions.assertNotNull(pedidos.get(0).getTipo());
        Assertions.assertNotNull(pedidos.get(0).getSituacao());
        Assertions.assertNotNull(pedidos.get(0).getDataCriacao());
        Assertions.assertNotNull(pedidos.get(0).getDataAtualizacao());
        Assertions.assertNotNull(pedidos.get(0).getDataFim());
    }

    @Test
    @DisplayName("Atualizar a data de finalização de um pedido")
    @Order(3)
    void atualizarDataFimTest() {
        final Instant instant = Instant.now();
        LocalDate expectedDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());
        Pedido pedidoOriginal = repository.findById(1L).orElse(null);

        pedidoOriginal.setDataFim(instant);
        repository.save(pedidoOriginal);

        Pedido response = repository.findById(1L).orElse(null);
        
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedDate, LocalDate.ofInstant(response.getDataFim(), ZoneId.systemDefault()));
    }

    @Test
    @DisplayName("Atualizar a situação de um pedido")
    @Order(4)
    void atualizarSituacaoTest() {
        STATUS_PEDIDO status = STATUS_PEDIDO.FINALIZADO;
        Pedido pedidoOriginal = repository.findById(3L).orElse(null);

        Assertions.assertNotEquals(status, pedidoOriginal.getSituacao());

        pedidoOriginal.setSituacao(status);
        repository.save(pedidoOriginal);

        Pedido response = repository.findById(3L).orElse(null);
        
        Assertions.assertNotNull(response);
        Assertions.assertEquals(status, response.getSituacao());
    }

    @Test
    @DisplayName("Criar um pedido")
    @Order(5)
    void criarPedidoTest() {
        long usuarioId = 5L;
        Pedido pedido = new Pedido();
        Usuario usuario = usuarioRepo.findById(usuarioId).orElse(null);
        Endereco endereco = enderecoRepository.findEnderecoByUsuarioId(usuarioId).get(0);

        pedido.setUsuario(usuario);
        pedido.setEndereco( endereco );
        pedido.setTipo(TIPO_PEDIDO.COMPRA);
        pedido.setSituacao(STATUS_PEDIDO.PENDENTE);
        pedido.setDataCriacao(Instant.now());
        pedido.setDataAtualizacao(Instant.now());

        List<ItemPedido> itens = new ArrayList<>();
        Double valorTotal = 0D;
        for (int i = 1; i < 3; i++) {
            ItemPedido item = new ItemPedido();
            Ferramenta ferramenta = ferramentaRepo.findById((long)i).get();
            item.setPedido(pedido);
            item.setFerramenta(ferramenta);
            item.setPrecoUnitario(BigDecimal.valueOf(ferramenta.getPreco_venda()));
            item.setQuantidade(1);
            valorTotal += ferramenta.getPreco_venda();
            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setValorTotal( BigDecimal.valueOf(valorTotal) );

        Pedido response = pedidoRepository.save(pedido);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getDataCriacao());
        Assertions.assertEquals(STATUS_PEDIDO.PENDENTE, response.getSituacao());
        Assertions.assertEquals(TIPO_PEDIDO.COMPRA, response.getTipo());
        Assertions.assertEquals(BigDecimal.valueOf(valorTotal), response.getValorTotal());

        List<Pedido> pedidosSalvos = repository.findPedidoByUsuarioId(usuarioId);
        Assertions.assertTrue(pedidosSalvos.size() > 1);
        Assertions.assertNotNull(pedidosSalvos.stream().filter(x -> x.getId() == response.getId()).findFirst());
    }
}
