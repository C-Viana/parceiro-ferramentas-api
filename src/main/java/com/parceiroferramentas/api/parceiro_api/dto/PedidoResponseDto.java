package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.parceiroferramentas.api.parceiro_api.model.pedido.StatusPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TipoPedido;

public record PedidoResponseDto(
    Long id,
    TipoPedido tipo,
    StatusPedido situacao,
    BigDecimal valorTotal,
    Instant dataCriacao,
    Instant dataFim,
    List<ItemCarrinhoResponseDto> itens,
    EnderecoDto endereco,
    PagamentoResponseDto pagamento
) {}
