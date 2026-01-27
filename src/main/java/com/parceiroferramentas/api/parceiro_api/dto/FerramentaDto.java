package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FerramentaDto (
    Long id,

    @NotBlank(message = "Nome da ferramenta não pode ser vazio")
    @NotNull(message = "Nome da ferramenta não pode ser nulo")
    @Size(min = 5, message = "Nome da ferramenta deve ter no mínimo 5 caracteres")
    String nome,

    @NotBlank(message = "Modelo da ferramenta não pode ser vazio")
    @NotNull(message = "Modelo da ferramenta não pode ser nulo")
    String modelo,

    @NotBlank(message = "Fabricante da ferramenta não pode ser vazio")
    @NotNull(message = "Fabricante da ferramenta não pode ser nulo")
    String fabricante,

    @NotBlank(message = "Tipo da ferramenta não pode ser vazio")
    @NotNull(message = "Tipo da ferramenta não pode ser nulo")
    String tipo,

    String descricao,
    Map<String, Object> caracteristicas,
    List<String> itens_inclusos,
    boolean disponibilidade,

    @NotNull(message = "Preço de aluguel da ferramenta não pode ser nulo")
    @DecimalMin(value = "1.0", message = "O preço de locação do produto não pode ser inferior a R$ 1.00")
    Double preco_aluguel,

    @NotNull(message = "Preço de venda da ferramenta não pode ser nulo")
    @DecimalMin(value = "1.0", message = "O preço de venda do produto não pode ser inferior a R$ 1.00")
    Double preco_venda
) {}
