package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;
import java.util.UUID;

public record UsuarioResponseDto(
    UUID id,
    String username,
    boolean enabled,
    List<PermissaoRequestDto> authorities
) {}
