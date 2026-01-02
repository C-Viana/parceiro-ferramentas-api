package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;

public record UsuarioResponseDto(
    Long id,
    String nome,
    String username,
    boolean enabled,
    List<PermissaoRequestDto> authorities
) {}
