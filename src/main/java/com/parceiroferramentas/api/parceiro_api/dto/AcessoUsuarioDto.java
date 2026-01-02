package com.parceiroferramentas.api.parceiro_api.dto;

import java.time.LocalDateTime;

public record AcessoUsuarioDto (
    String username,
    boolean autenticado,
    LocalDateTime inicio,
    LocalDateTime fim,
    String acesso,
    String renovacao
) {}
