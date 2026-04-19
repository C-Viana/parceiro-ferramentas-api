package com.parceiroferramentas.api.parceiro_api.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
    UUID id,
    
    @NotBlank(message = "Nome de usuário não pode estar vazio")
    @NotNull(message = "Nome de usuário não pode ser nulo")
    @Size(min = 5, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
    String username,
    
    @NotBlank(message = "Senha não pode estar vazia")
    @NotNull(message = "Senha não pode ser nula")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String senha,

    boolean enabled,
    List<PermissaoRequestDto> authorities
) {}
