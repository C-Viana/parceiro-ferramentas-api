package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.parceiroferramentas.api.parceiro_api.model.pagamento.TipoPagamento;

public record PagamentoResponseDto(
    TipoPagamento formaPagamento,
    BigDecimal valor,
    Instant dataCriacao,
    String detalhes
) {}
