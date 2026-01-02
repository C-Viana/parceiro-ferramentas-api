package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;
import java.util.Map;

public record FerramentaDto (
    Long id,
    String nome,
    String modelo,
    String fabricante,
    String tipo,
    String descricao,
    Map<String, Object> caracteristicas,
    List<String> itens_inclusos,
    boolean disponibilidade,
    Double preco_aluguel,
    Double preco_venda
) {}
