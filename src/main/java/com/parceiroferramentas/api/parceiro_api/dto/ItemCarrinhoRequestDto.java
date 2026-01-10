package com.parceiroferramentas.api.parceiro_api.dto;

public record ItemCarrinhoRequestDto(
    Long ferramenta_id,
    Integer quantidade
)
{}
