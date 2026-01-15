package com.parceiroferramentas.api.parceiro_api.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;
import com.parceiroferramentas.api.parceiro_api.exception.NotFoundException;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.Pagamento;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.PagamentoStrategy;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.STATUS_PAGAMENTO;
import com.parceiroferramentas.api.parceiro_api.model.pedido.ItemPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.STATUS_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TIPO_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.EnderecoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.ItemPedidoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PagamentoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.PedidoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ItemPedidoRepository itemRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private JwtTokenService tokenService;

    //private Logger logger = LoggerFactory.getLogger(EnderecoController.class);

    public LocalDate validarTextoDeData( String textoData ) {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate date = LocalDate.parse(textoData, DATE_FORMATTER);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    private String extrairUsername(String jwtAccessToken) {
        if(jwtAccessToken == null || !jwtAccessToken.startsWith("Bearer")) {
            throw new InvalidAuthorizationException("O token informado está nulo ou é inválido");
        }
        return tokenService.decodeToken(jwtAccessToken.split(" ")[1]).getSubject();
    }

    private BigDecimal calcularValorTotalCompraCarrinho(List<ItemCarrinho> itens) {
        return itens
            .stream()
            .map( item -> item.getPrecoVendaMomento().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalAluguelCarrinho(List<ItemCarrinho> itens) {
        return itens
            .stream()
            .map( item -> item.getPrecoAluguelMomento().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Pedido criarPedidoCompra(String token, Long enderecoId, PagamentoStrategy pagamentoReq, String detalhesPagamento) {
        Pedido pedido = new Pedido();
        String username = extrairUsername(token);
        Usuario usuario = usuarioRepo.findUsuarioByUsername(username);
        Endereco endereco = enderecoRepository.findById(enderecoId).orElseThrow(() -> new BadRequestException("Endereço não encontrado"));

        if(usuario.getId() != endereco.getUsuario().getId()) throw new BadRequestException("Foi identificada uma inconsistência com os dados de endereço");

        List<ItemPedido> itens = new ArrayList<>();

        pedido.setUsuario(usuario);
        pedido.setEndereco( endereco );
        pedido.setTipo(TIPO_PEDIDO.COMPRA);
        pedido.setSituacao(STATUS_PEDIDO.PENDENTE);
        pedido.setDataCriacao(Instant.now());
        pedido.setDataAtualizacao(Instant.now());

        usuario.getCarrinhoItens().forEach( itemCarrinho -> {
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setFerramenta(itemCarrinho.getFerramenta());
            item.setPrecoUnitario(itemCarrinho.getPrecoVendaMomento());
            item.setQuantidade(itemCarrinho.getQuantidade());
            itens.add(item);
        });
        
        pedido.setItens(itens);
        pedido.setValorTotal( calcularValorTotalCompraCarrinho(usuario.getCarrinhoItens()) );

        Pedido pedidoCriado = pedidoRepository.save(pedido);
        if(pedidoCriado == null) throw new RuntimeException("ERRO AO CRIAR O PEDIDO PARA O USUARIO " + usuario.getUsername());

        itemRepository.saveAll(itens);

        Pagamento pagamento = pagamentoReq.processar(pedidoCriado, detalhesPagamento);
        pagamento.setPedido(pedidoCriado);
        pagamento.setDataCriacao(Instant.now());
        pagamento.setDataAtualizacao(Instant.now());
        pagamentoRepository.save(pagamento);

        if (pagamento.getSituacao() == STATUS_PAGAMENTO.APROVADO) {
            pedidoCriado.setPagamento(pagamento);
            pedidoCriado.setSituacao(STATUS_PEDIDO.APROVADO);
            pedidoRepository.save(pedidoCriado);
            carrinhoRepository.deleteAll(usuario.getCarrinhoItens());
            usuario.getCarrinhoItens().clear();
        }

        return pedido;
    }

    public Pedido criarPedidoAluguel(String token, Long prazo, Long enderecoId, PagamentoStrategy pagamentoReq, String detalhesPagamento) {
        if(prazo < 1) throw new BadRequestException("O prazo de locação deve ser maior que ZERO");
        Pedido pedido = new Pedido();
        String username = extrairUsername(token);
        Usuario usuario = usuarioRepo.findUsuarioByUsername(username);
        Endereco endereco = enderecoRepository.findById(enderecoId).orElseThrow(() -> new BadRequestException("Endereço não encontrado"));

        if(usuario.getId() != endereco.getUsuario().getId()) throw new BadRequestException("Foi identificada uma inconsistência com os dados de endereço");

        List<ItemPedido> itens = new ArrayList<>();

        pedido.setUsuario(usuario);
        pedido.setEndereco( endereco );
        pedido.setTipo(TIPO_PEDIDO.ALUGUEL);
        pedido.setSituacao(STATUS_PEDIDO.PENDENTE);
        pedido.setDataCriacao(Instant.now());
        pedido.setDataAtualizacao(Instant.now());
        pedido.setDataFim(Instant.now().plus(prazo, ChronoUnit.DAYS));

        usuario.getCarrinhoItens().forEach( itemCarrinho -> {
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setFerramenta(itemCarrinho.getFerramenta());
            item.setPrecoUnitario(itemCarrinho.getPrecoAluguelMomento());
            item.setQuantidade(itemCarrinho.getQuantidade());
            itens.add(item);
        });
        
        pedido.setItens(itens);
        BigDecimal valorFinal = calcularValorTotalAluguelCarrinho(usuario.getCarrinhoItens()).multiply(BigDecimal.valueOf(prazo));
        pedido.setValorTotal( valorFinal );

        Pedido pedidoCriado = pedidoRepository.save(pedido);
        if(pedidoCriado == null) throw new RuntimeException("ERRO AO CRIAR O PEDIDO PARA O USUARIO " + usuario.getUsername());

        itemRepository.saveAll(itens);

        Pagamento pagamento = pagamentoReq.processar(pedidoCriado, detalhesPagamento);
        pagamento.setPedido(pedidoCriado);
        pagamento.setDataCriacao(Instant.now());
        pagamento.setDataAtualizacao(Instant.now());
        pagamentoRepository.save(pagamento);

        if (pagamento.getSituacao() == STATUS_PAGAMENTO.APROVADO) {
            pedidoCriado.setPagamento(pagamento);
            pedidoCriado.setSituacao(STATUS_PEDIDO.APROVADO);
            pedidoRepository.save(pedidoCriado);
            carrinhoRepository.deleteAll(usuario.getCarrinhoItens());
            usuario.getCarrinhoItens().clear();
        }

        return pedido;
    }

    public Pedido buscarPedido(Long pedidoId) {
        return pedidoRepository.findById(pedidoId).orElse(null);
    }

    public List<Pedido> buscarPedidosDoUsuario(String token) {
        String username = extrairUsername(token);
        Usuario usuario = usuarioRepo.findUsuarioByUsername(username);
        return pedidoRepository.findPedidoByUsuarioId(usuario.getId());
    }

    public Pedido atualizarDataFim(Long pedidoId, String textoDataNova) {
        LocalTime horaAtual = LocalTime.now();
        LocalDateTime novaData = validarTextoDeData(textoDataNova).atTime(horaAtual);

        if( novaData == null )
            throw new BadRequestException("A DATA INFORMADA NAO E VALIDA. VERIFIQUE SE O FORMADO ESTA DENTRO DA REGRA [dd-MM-yyyy]");

        if( novaData.toLocalDate().isBefore(LocalDate.now()) ) throw new BadRequestException("A NOVA DATA NAO PODE SER ANTERIOR AO DIA ATUAL");
        
        Pedido entidade = pedidoRepository.findById(pedidoId).orElseThrow(() -> new NotFoundException("PEDIDO ID ["+pedidoId+"] NAO FOI ENCONTRADO"));

        entidade.setDataFim(novaData.toInstant(ZoneOffset.UTC));
        
        return pedidoRepository.save(entidade);
    }

    public Pedido atualizarSituacao(Long pedidoId, STATUS_PEDIDO novaSituacao) {
        if( novaSituacao == null )
            throw new BadRequestException("A SITUACAO NAO FOI RECEBIDA. INFORME UM VALOR DE ACORDO COM O PADRAO -> " + STATUS_PEDIDO.values());

        Pedido entidade = pedidoRepository.findById(pedidoId).orElseThrow(() -> new NotFoundException("PEDIDO ID ["+pedidoId+"] NAO FOI ENCONTRADO"));

        entidade.setSituacao(novaSituacao);
        entidade.setDataAtualizacao(Instant.now());

        return pedidoRepository.save(entidade);
    }

}
