package com.parceiroferramentas.api.parceiro_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CredenciaisUsuarioDto (
    @NotBlank(message = "Nome de usuário não pode estar vazio")
    @Size(min = 5, max = 50, message = "Nome deve ter entre 5 e 50 caracteres")
    String username,

    @NotBlank(message = "Senha não pode estar vazia")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String senha
)
{}
