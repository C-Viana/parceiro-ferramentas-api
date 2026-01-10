package com.parceiroferramentas.api.parceiro_api.dto;

public record ItemCarrinhoRequestDto(
    Long ferramenta_id,
    Integer quantidade
)
{
    public ItemCarrinhoRequestDto(Long ferramenta_id, Integer quantidade) {
        this.ferramenta_id = ferramenta_id;
        this.quantidade = quantidade;
    }
}
