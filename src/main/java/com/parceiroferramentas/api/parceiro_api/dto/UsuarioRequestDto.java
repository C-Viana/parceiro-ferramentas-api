package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;

public record UsuarioRequestDto(
    Long id,
    String nome,
    String username,
    String senha,
    boolean enabled,
    List<PermissaoRequestDto> authorities
) {}
