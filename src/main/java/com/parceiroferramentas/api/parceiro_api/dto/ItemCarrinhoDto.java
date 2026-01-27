package com.parceiroferramentas.api.parceiro_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemCarrinhoDto(
    Long id,

    Ferramenta ferramenta,

    @NotNull(message = "A quantidade do item está nula")
    @Min(value = 1, message = "A quantidade mínima do item do carrinho deve ser 1 (UM)")
    Integer quantidade,

    @NotNull(message = "Preço de venda da ferramenta não pode ser nulo")
    @DecimalMin(value = "1.0", message = "O preço de venda do produto não pode ser inferior a R$ 1.00")
    @JsonProperty(value = "preco_venda_momento")
    BigDecimal precoVendaMomento,
    
    @NotNull(message = "Preço de aluguel da ferramenta não pode ser nulo")
    @DecimalMin(value = "1.0", message = "O preço de locação do produto não pode ser inferior a R$ 1.00")
    @JsonProperty(value = "preco_aluguel_momento")
    BigDecimal precoAluguelMomento,
    
    @DefaultValue(value = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg")
    String urlImage,
    
    @JsonProperty(value = "data_adicao")
    Instant dataAdicao
) {}
