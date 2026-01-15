package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;

public record FerramentaPedidoDto (
    String nome,
    String modelo,
    String fabricante,
    String tipo,
    List<String> itens_inclusos
) {}
