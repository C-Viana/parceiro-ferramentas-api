package com.parceiroferramentas.api.parceiro_api.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.DebitoStrategy;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.Pagamento;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.PixStrategy;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.TipoPagamento;
import com.parceiroferramentas.api.parceiro_api.model.pedido.ItemPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.StatusPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TipoPedido;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.EnderecoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.ItemPedidoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PagamentoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PedidoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.MetricsService;
import com.parceiroferramentas.api.parceiro_api.service.PedidoService;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock private MetricsService metricsService;
    @Mock private CarrinhoRepository carrinhoRepository;
    @Mock private ItemPedidoRepository itemRepository;
    @Mock private PedidoRepository pedidoRepository;
    @Mock private UsuarioRepository usuarioRepo;
    @Mock private EnderecoRepository enderecoRepository;
    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private JwtTokenService tokenService;

    @InjectMocks PedidoService service;

    static Comprador comprador;
    static Usuario usuario;
    static List<Ferramenta> ferramentas;
    static List<ItemCarrinho> carrinho;
    static List<Endereco> enderecos;
    static final String ACCESS_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkdFUkVOVEUiLCJWRU5ERURPUiIsIkNMSUVOVEUiXSwiaWF0IjoxNzY2MTUxNDkwLCJleHAiOjE3NjYxNTUwOTAsInN1YiI6IjgwNjkwNTcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.DnqrSTnvUMVasSb1yr9iJQGP4euPlkFXxbFJqSwFdWw";

    @BeforeAll
    public static void setup() {
        usuario = CreateMockedData.getInstance().getUsuarios().get(3);
        comprador = CreateMockedData.getInstance().getCompradores().get(3);
        ferramentas = CreateMockedData.getInstance().getFerramentas();
        enderecos = CreateMockedData.getInstance().getEnderecos( CreateMockedData.getInstance().getCompradores());
    }

    @Test
    @DisplayName("Deve criar um pedido de compra")
    void criarPedidoCompraTeste() {
        int tamanhoCarrinho = 2;
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        String pagamentoDetalhesJson = "{\r\n" + //
                        "    \"forma_pagamento\": \"PIX\",\r\n" + //
                        "    \"valor\": 2188.58,\r\n" + //
                        "    \"detalhes\": {\r\n" + //
                        "        \"nome\": \"Lindosvaldo Melo da Silva\",\r\n" + //
                        "        \"documento:\": \"46854810201\",\r\n" + //
                        "        \"banco\": \"Nubank\",\r\n" + //
                        "        \"agencia\": \"00001\",\r\n" + //
                        "        \"conta\": \"10852432\",\r\n" + //
                        "        \"digito\": \"2\"\r\n" + //
                        "    }\r\n" + //
                        "}";
        
        List<ItemPedido> itens = CreateMockedData.getInstance().getItensDoPedidoCompra(comprador.getCarrinhoItens());
        Pedido pedidoAntes = CreateMockedData.getInstance().getPedido(TipoPedido.COMPRA, 0, comprador, enderecos.get(3), itens);
        Pagamento pagamento = new PixStrategy().processar(pedidoAntes, pagamentoDetalhesJson);
        
        Pedido pedidoComId = pedidoAntes;
        pedidoComId.setId(1L);
        
        Pedido pedidoPago = pedidoComId;
        pedidoPago.setPagamento(pagamento);
        pedidoPago.setSituacao(StatusPedido.CRIADO);

        DecodedJWT decodedToken = Mockito.mock(DecodedJWT.class);
        
        Mockito.when(decodedToken.getSubject()).thenReturn(usuario.getUsername());
        Mockito.when(tokenService.decodeToken(Mockito.anyString())).thenReturn(decodedToken);
        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(enderecoRepository.findById(enderecos.get(3).getId()))
            .thenReturn(
                Optional.of(enderecos.get(3))
            );
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
        
        // Mock para as duas chamadas de save: primeira retorna com ID, segunda retorna com pagamento
        Mockito.when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(pedidoComId)
            .thenReturn(pedidoPago);
        
        Mockito.doNothing().when(carrinhoRepository).deleteAll(any());

        Pedido response = service.criarPedidoCompra(ACCESS_TOKEN, enderecos.get(3).getId(), new PixStrategy(), pagamentoDetalhesJson);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(tamanhoCarrinho, response.getItens().size());
        Assertions.assertEquals(TipoPagamento.PIX_DYNAMIC, response.getPagamento().getFormaPagamento());
        Assertions.assertEquals(BigDecimal.valueOf(2188.58), response.getValorTotal());

        Mockito.verify(pedidoRepository, times(2)).save(any());
        Mockito.verify(pagamentoRepository, times(1)).save(any());
        Mockito.verify(metricsService).registrarPedidoCriado("COMPRA");
    }

    @Test
    @DisplayName("Deve criar um pedido de aluguel")
    void criarPedidoAluguelTeste() {
        int tamanhoCarrinho = 2;
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        String pagamentoDetalhesJson = "{\r\n" + //
                        "    \"forma_pagamento\": \"DEBITO\",\r\n" + //
                        "    \"valor\": 1219.00,\r\n" + //
                        "    \"detalhes\": {\r\n" + //
                        "        \"nome\": \"Lindosvaldo Melo da Silva\",\r\n" + //
                        "        \"documento:\": \"46854810201\",\r\n" + //
                        "        \"banco\": \"Nubank\",\r\n" + //
                        "        \"agencia\": \"00001\",\r\n" + //
                        "        \"conta\": \"10852432\",\r\n" + //
                        "        \"digito\": \"2\"\r\n" + //
                        "    }\r\n" + //
                        "}";
        
        List<ItemPedido> itens = CreateMockedData.getInstance().getItensDoPedidoAluguel(comprador.getCarrinhoItens());
        Pedido pedidoAntes = CreateMockedData.getInstance().getPedido(TipoPedido.ALUGUEL, 10, comprador, enderecos.get(3), itens);
        Pagamento pagamento = new PixStrategy().processar(pedidoAntes, pagamentoDetalhesJson);
        
        Pedido pedidoComId = pedidoAntes;
        pedidoComId.setId(1L);
        
        Pedido pedidoPago = pedidoComId;
        pedidoPago.setPagamento(pagamento);
        pedidoPago.setSituacao(StatusPedido.CRIADO);

        DecodedJWT decodedToken = Mockito.mock(DecodedJWT.class);
        
        Mockito.when(decodedToken.getSubject()).thenReturn(usuario.getUsername());
        Mockito.when(tokenService.decodeToken(Mockito.anyString())).thenReturn(decodedToken);
        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(enderecoRepository.findById(enderecos.get(3).getId()))
            .thenReturn(
                Optional.of(enderecos.get(3))
            );
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
        
        Mockito.when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(pedidoComId)
            .thenReturn(pedidoPago);
        
        Mockito.doNothing().when(carrinhoRepository).deleteAll(any());

        System.out.println( "LOG >>>>>>>>>>>> SIZE " + comprador.getCarrinhoItens().size() );
        System.out.println( "LOG >>>>>>>>>>>> ITEM #1 " + comprador.getCarrinhoItens().get(0).getPrecoAluguelMomento() );
        System.out.println( "LOG >>>>>>>>>>>> ITEM #1 " + comprador.getCarrinhoItens().get(0).getQuantidade() );
        System.out.println( "LOG >>>>>>>>>>>> ITEM #2 " + comprador.getCarrinhoItens().get(1).getPrecoAluguelMomento() );
        System.out.println( "LOG >>>>>>>>>>>> ITEM #2 " + comprador.getCarrinhoItens().get(1).getQuantidade() );
        
        Pedido response = service.criarPedidoAluguel(ACCESS_TOKEN, 10L, enderecos.get(3).getId(), new DebitoStrategy(), pagamentoDetalhesJson);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(tamanhoCarrinho, response.getItens().size());
        Assertions.assertEquals(TipoPagamento.DEBITO, response.getPagamento().getFormaPagamento());
        Assertions.assertEquals(BigDecimal.valueOf(1219.00), response.getValorTotal());

        Mockito.verify(pedidoRepository, times(2)).save(any());
        Mockito.verify(pagamentoRepository, times(1)).save(any());
        Mockito.verify(metricsService).registrarPedidoCriado("ALUGUEL");
    }

    @Test
    @DisplayName("Deve buscar um pedido pelo seu ID")
    void buscarPedidoTeste() {
        int tamanhoCarrinho = 3;
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        String pagamentoDetalhesJson = "{\r\n" + //
                        "    \"forma_pagamento\": \"DEBITO\",\r\n" + //
                        "    \"valor\": 3178.28,\r\n" + //
                        "    \"detalhes\": {\r\n" + //
                        "        \"nome\": \"Lindosvaldo Melo da Silva\",\r\n" + //
                        "        \"documento:\": \"46854810201\",\r\n" + //
                        "        \"banco\": \"Nubank\",\r\n" + //
                        "        \"agencia\": \"00001\",\r\n" + //
                        "        \"conta\": \"10852432\",\r\n" + //
                        "        \"digito\": \"2\"\r\n" + //
                        "    }\r\n" + //
                        "}";
        
        List<ItemPedido> itens = CreateMockedData.getInstance().getItensDoPedidoCompra(comprador.getCarrinhoItens());
        Pedido pedidoModel = CreateMockedData.getInstance().getPedido(TipoPedido.COMPRA, 0, comprador, enderecos.get(3), itens);
        pedidoModel.setId(1L);
        Pagamento pagamento = new DebitoStrategy().processar(pedidoModel, pagamentoDetalhesJson);
        pedidoModel.setPagamento(pagamento);
        pedidoModel.setSituacao(StatusPedido.CRIADO);

        Mockito.when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedidoModel));

        Pedido response = service.buscarPedido(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(tamanhoCarrinho, response.getItens().size());
        Assertions.assertEquals(TipoPagamento.DEBITO, response.getPagamento().getFormaPagamento());
        Assertions.assertEquals(BigDecimal.valueOf(3178.28), response.getValorTotal());

        Mockito.verify(pedidoRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Deve buscar a lista de pedidos de um cliente")
    void buscarPedidosDoUsuarioTeste() {
        int tamanhoCarrinho = 3;
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        String pagamentoDetalhesJson = "{\r\n" + //
                        "    \"forma_pagamento\": \"DEBITO\",\r\n" + //
                        "    \"valor\": 1219.00,\r\n" + //
                        "    \"detalhes\": {\r\n" + //
                        "        \"nome\": \"Lindosvaldo Melo da Silva\",\r\n" + //
                        "        \"documento:\": \"46854810201\",\r\n" + //
                        "        \"banco\": \"Nubank\",\r\n" + //
                        "        \"agencia\": \"00001\",\r\n" + //
                        "        \"conta\": \"10852432\",\r\n" + //
                        "        \"digito\": \"2\"\r\n" + //
                        "    }\r\n" + //
                        "}";
        
        List<ItemPedido> itens = CreateMockedData.getInstance().getItensDoPedidoCompra(comprador.getCarrinhoItens());
        List<Pedido> pedidosModel = new ArrayList<>();
        Pedido pedidoModel = CreateMockedData.getInstance().getPedido(TipoPedido.COMPRA, 0, comprador, enderecos.get(3), itens);
        pedidoModel.setId(1L);
        Pagamento pagamento = new PixStrategy().processar(pedidoModel, pagamentoDetalhesJson);
        pedidoModel.setPagamento(pagamento);
        pedidoModel.setSituacao(StatusPedido.CRIADO);
        pedidosModel.add(pedidoModel);

        DecodedJWT decodedToken = Mockito.mock(DecodedJWT.class);
        
        Mockito.when(decodedToken.getSubject()).thenReturn(usuario.getUsername());
        Mockito.when(tokenService.decodeToken(Mockito.anyString())).thenReturn(decodedToken);
        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(pedidoRepository.findPedidoByCompradorId(comprador.getId())).thenReturn(pedidosModel);

        List<Pedido> response = service.buscarPedidosDoUsuario(ACCESS_TOKEN);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size(), "Quantidade de pedidos deve ser 1 (UM)");
        Assertions.assertEquals(TipoPagamento.PIX_DYNAMIC, response.get(0).getPagamento().getFormaPagamento());
        Assertions.assertEquals(BigDecimal.valueOf(3178.28), response.get(0).getValorTotal());

        Mockito.verify(pedidoRepository, times(1)).findPedidoByCompradorId(any());
    }

    @Test
    @DisplayName("Deve atualizar a data de finalização de um pedido")
    void atualizarDataFimTeste() {
        int tamanhoCarrinho = 3;
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        String pagamentoDetalhesJson = "{\r\n" + //
                        "    \"forma_pagamento\": \"DEBITO\",\r\n" + //
                        "    \"valor\": 3178.28,\r\n" + //
                        "    \"detalhes\": {\r\n" + //
                        "        \"nome\": \"Lindosvaldo Melo da Silva\",\r\n" + //
                        "        \"documento:\": \"46854810201\",\r\n" + //
                        "        \"banco\": \"Nubank\",\r\n" + //
                        "        \"agencia\": \"00001\",\r\n" + //
                        "        \"conta\": \"10852432\",\r\n" + //
                        "        \"digito\": \"2\"\r\n" + //
                        "    }\r\n" + //
                        "}";
        
        List<ItemPedido> itens = CreateMockedData.getInstance().getItensDoPedidoCompra(comprador.getCarrinhoItens());
        Pedido pedidoModel = CreateMockedData.getInstance().getPedido(TipoPedido.COMPRA, 0, comprador, enderecos.get(3), itens);
        pedidoModel.setId(1L);
        Pagamento pagamento = new DebitoStrategy().processar(pedidoModel, pagamentoDetalhesJson);
        pedidoModel.setPagamento(pagamento);
        pedidoModel.setSituacao(StatusPedido.CRIADO);

        Pedido pedidoAtualizado = pedidoModel;
        pedidoAtualizado.setDataFim(Instant.now());

        Mockito.when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedidoModel));
        Mockito.when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(pedidoAtualizado);

        Pedido response = service.atualizarDataFim(1L, "20-12-2026");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(LocalDate.of(2026, 12, 20), LocalDate.ofInstant(response.getDataFim(), ZoneId.systemDefault()));

        Mockito.verify(pedidoRepository, times(1)).findById(any());
        Mockito.verify(pedidoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve atualizar a situação de um pedido")
    void atualizarSituacao() {
        int tamanhoCarrinho = 3;
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        String pagamentoDetalhesJson = "{\r\n" + //
                        "    \"forma_pagamento\": \"DEBITO\",\r\n" + //
                        "    \"valor\": 3178.28,\r\n" + //
                        "    \"detalhes\": {\r\n" + //
                        "        \"nome\": \"Lindosvaldo Melo da Silva\",\r\n" + //
                        "        \"documento:\": \"46854810201\",\r\n" + //
                        "        \"banco\": \"Nubank\",\r\n" + //
                        "        \"agencia\": \"00001\",\r\n" + //
                        "        \"conta\": \"10852432\",\r\n" + //
                        "        \"digito\": \"2\"\r\n" + //
                        "    }\r\n" + //
                        "}";
        
        List<ItemPedido> itens = CreateMockedData.getInstance().getItensDoPedidoCompra(comprador.getCarrinhoItens());
        Pedido pedidoModel = CreateMockedData.getInstance().getPedido(TipoPedido.COMPRA, 0, comprador, enderecos.get(3), itens);
        pedidoModel.setId(1L);
        Pagamento pagamento = new DebitoStrategy().processar(pedidoModel, pagamentoDetalhesJson);
        pedidoModel.setPagamento(pagamento);
        pedidoModel.setSituacao(StatusPedido.CRIADO);

        Pedido pedidoAtualizado = pedidoModel;
        pedidoAtualizado.setDataFim(Instant.now());

        Mockito.when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedidoModel));
        Mockito.when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(pedidoAtualizado);

        Pedido response = service.atualizarSituacao(1L, StatusPedido.CANCELADO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(StatusPedido.CANCELADO, response.getSituacao());
        Assertions.assertEquals(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()), LocalDate.ofInstant(response.getDataFim(), ZoneId.systemDefault()));

        Mockito.verify(pedidoRepository, times(1)).findById(any());
        Mockito.verify(pedidoRepository, times(1)).save(any());
    }
}
