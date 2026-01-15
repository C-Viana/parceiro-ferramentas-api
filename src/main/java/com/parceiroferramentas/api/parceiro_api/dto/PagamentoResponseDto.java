package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.parceiroferramentas.api.parceiro_api.model.pagamento.TIPO_PAGAMENTO;

public record PagamentoResponseDto(
    TIPO_PAGAMENTO formaPagamento,
    BigDecimal valor,
    Instant dataCriacao,
    String detalhes
) {}
