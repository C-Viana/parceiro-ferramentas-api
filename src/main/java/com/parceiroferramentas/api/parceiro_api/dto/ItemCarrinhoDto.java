package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;

public record ItemCarrinhoDto(
    Long id,
    Ferramenta ferramenta,
    Integer quantidade,
    @JsonProperty(value = "preco_venda_momento")
    BigDecimal precoVendaMomento,
    @JsonProperty(value = "preco_aluguel_momento")
    BigDecimal precoAluguelMomento,
    String urlImage,
    @JsonProperty(value = "data_adicao")
    Instant dataAdicao
) {
    
}
