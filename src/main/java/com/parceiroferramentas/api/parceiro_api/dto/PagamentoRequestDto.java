package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.util.Map;

public record PagamentoRequestDto(
    BigDecimal valor,
    String formaPagamento,
    Map<String, Object> detalhes
) {}
