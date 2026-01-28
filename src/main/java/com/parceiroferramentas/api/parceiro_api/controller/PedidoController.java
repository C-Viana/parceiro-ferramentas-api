package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parceiroferramentas.api.parceiro_api.controller.openapi.PedidoDocumentation;
import com.parceiroferramentas.api.parceiro_api.dto.PagamentoRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.PedidoResponseDto;
import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.DebitoStrategy;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.PagamentoStrategy;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.PixStrategy;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.TIPO_PAGAMENTO;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.STATUS_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.service.PedidoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/pedido")
public class PedidoController implements PedidoDocumentation {

    @Autowired
    PedidoService pedidoService;

    @Autowired
    private GlobalObjectMapper mapper;

    private PagamentoStrategy setPagamento(PagamentoRequestDto dto) {
        PagamentoStrategy pagamentoStrategy;
        log.info("DADOS DE PAGAMENTO: " + dto.toString());
        
        switch (TIPO_PAGAMENTO.getByDisplayValue(dto.formaPagamento())) {
            case PIX:
                pagamentoStrategy = new PixStrategy();
                return pagamentoStrategy;
            case DEBITO:
                pagamentoStrategy = new DebitoStrategy();
                return pagamentoStrategy;
            default:
                throw new BadRequestException("TIPO DE PAGAMENTO INVALIDO OU NAO ACEITO EM NOSSA OPERACAO");
        }
    }

    @Override
    @PostMapping(value = "/venda/endereco/{enderecoId}",
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<PedidoResponseDto> criarPedidoDeCompra(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @PathVariable Long enderecoId, 
        @Valid
        @RequestBody PagamentoRequestDto pagamento
    ) throws JsonProcessingException {
        log.info("CRIANDO PEDIDO DE COMPRA");
        String jsonDetalhesPagamento = new ObjectMapper().writeValueAsString(pagamento.detalhes());
        Pedido pedidoCriado = pedidoService.criarPedidoCompra(token, enderecoId, setPagamento(pagamento), jsonDetalhesPagamento);

        PedidoResponseDto response = mapper.toPedidoResponseDto(pedidoCriado);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedidoCriado.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @PostMapping(value = "/aluguel/{diasPrazo}/endereco/{enderecoId}",
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<PedidoResponseDto> criarPedidoDeAluguel(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @Valid
        @NotNull(message = "O prazo de locação não foi informado")
        @Min(value = 1, message = "O prazo de locação deve ser de pelo menos 1 dia.")
        @PathVariable Long diasPrazo, 
        @Valid
        @NotNull(message = "O identificador de endereço não foi informado")
        @Min(value = 1, message = "O identificador de endereço não pode ser inferior a 1")
        @PathVariable Long enderecoId, 
        @Valid
        @RequestBody PagamentoRequestDto pagamento
    ) throws JsonProcessingException {
        log.info("CRIANDO PEDIDO DE ALUGUEL");
        String jsonDetalhesPagamento = new ObjectMapper().writeValueAsString(pagamento.detalhes());
        Pedido pedidoCriado = pedidoService.criarPedidoAluguel(token, diasPrazo, enderecoId, setPagamento(pagamento), jsonDetalhesPagamento);

        PedidoResponseDto response = mapper.toPedidoResponseDto(pedidoCriado);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedidoCriado.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @GetMapping(value = "/{pedidoId}", produces = "application/json")
    public ResponseEntity<PedidoResponseDto> buscarPedidoPorId(@PathVariable Long pedidoId) {
        log.info("RECUPERANDO O PEDIDO " + pedidoId);
        Pedido entidade = pedidoService.buscarPedido(pedidoId);
        return ResponseEntity.ok(mapper.toPedidoResponseDto(entidade));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> buscarPedidosDoUsuario(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token
    ) {
        log.info("RECUPERANDO OS PEDIDOS DO USUARIO");
        List<Pedido> pedidos = pedidoService.buscarPedidosDoUsuario(token);
        
        List<PedidoResponseDto> response = pedidos.stream().map( mapper::toPedidoResponseDto ).toList();

        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping(value = "/datafim", produces = "application/json")
    public ResponseEntity<PedidoResponseDto> atualizarDataFimDoPedido(
        @Valid
        @NotNull(message = "O parâmetro de identificação do pedido está nulo")
        @Min(value = 1, message = "O identificador do pedido não pode ser inferior a 1")
        @RequestParam(name = "pedido_id", required = true) Long pedido_id,
        @Valid
        @NotNull(message = "O parâmetro data (dd/mm/yyyy) não foi informado")
        @NotBlank(message = "O parâmetro data (dd/mm/yyyy) foi enviado vazio")
        @Pattern(regexp = "[0-9]{2}-[0-9]{2}-[0-9]{4}", message = "O formato da data deve seguir o padrão dd-MM-yyyy")
        @RequestParam(name = "nova_data", required = true) String nova_data
    ) {
        Pedido pedido = pedidoService.atualizarDataFim(pedido_id, nova_data);
        return ResponseEntity.ok(mapper.toPedidoResponseDto(pedido));
    }

    @Override
    @PatchMapping(value = "/situacao", produces = "application/json")
    public ResponseEntity<PedidoResponseDto> atualizarSituacaoDoPedido(
        @Valid
        @NotNull(message = "O parâmetro de identificação do pedido está nulo")
        @RequestParam(name = "pedido_id", required = true) Long pedido_id,
        @Valid
        @NotNull(message = "O parâmetro situação não foi informado")
        @NotBlank(message = "O parâmetro situação foi enviado vazio")
        @RequestParam(name = "nova_situacao", required = true) String nova_situacao
    ) {
        Pedido pedido = pedidoService.atualizarSituacao(pedido_id, STATUS_PEDIDO.getByDisplayValue(nova_situacao));
        return ResponseEntity.ok(mapper.toPedidoResponseDto(pedido));
    }

}
