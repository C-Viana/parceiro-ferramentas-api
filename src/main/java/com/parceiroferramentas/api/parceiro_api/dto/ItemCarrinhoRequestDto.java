package com.parceiroferramentas.api.parceiro_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemCarrinhoRequestDto(
    @NotNull(message = "O ID da ferramenta está nulo")
    @Min(value = 1, message = "O identificador da ferramenta não pode ser menor que 1 (UM)")
    Long ferramenta_id,

    @NotNull(message = "A quantidade do item está nula")
    @Min(value = 1, message = "A quantidade mínima do item do carrinho deve ser 1 (UM)")
    Integer quantidade
)
{}
