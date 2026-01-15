package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.parceiroferramentas.api.parceiro_api.model.pedido.STATUS_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TIPO_PEDIDO;

public record PedidoResponseDto(
    Long id,
    TIPO_PEDIDO tipo,
    STATUS_PEDIDO situacao,
    BigDecimal valorTotal,
    Instant dataCriacao,
    Instant dataFim,
    List<ItemCarrinhoResponseDto> itens,
    EnderecoDto endereco,
    PagamentoResponseDto pagamento
) {}
