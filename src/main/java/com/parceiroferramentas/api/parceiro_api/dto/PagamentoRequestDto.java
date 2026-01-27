package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDto(
    @NotNull(message = "O valor do pagamento não pode ser nulo")
    @DecimalMin(value = "0.1", message = "O valor do pagamento deve ser maior que zero")
    BigDecimal valor,

    @NotNull(message = "A forma de pagamento não pode ser nula")
    @NotBlank(message = "A forma de pagamento deve ser especificada")
    String formaPagamento,

    @NotNull(message = "O campo de detalhes do pagamento não pode ser nulo")
    Map<String, Object> detalhes
) {}
