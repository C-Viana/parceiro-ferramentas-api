package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;

public record ItemCarrinhoResponseDto(
    FerramentaPedidoDto ferramenta,
    Integer quantidade,
    BigDecimal precoUnitario
) {}
