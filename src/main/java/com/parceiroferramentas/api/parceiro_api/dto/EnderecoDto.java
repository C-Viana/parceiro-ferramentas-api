package com.parceiroferramentas.api.parceiro_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EnderecoDto(
    @NotBlank(message = "O campo lugradouro não pode ser vazio")
    @NotNull(message = "O campo lugradouro não pode ser nulo")
    @Size(min = 4, message = "O campo lugradouro deve conter pelo menos 4 caracteres")
    String logradouro,
    
    @NotNull(message = "O campo número do endereço não pode ser nulo")
    @Min(value = 1, message = "O campo número do endereço não pode ser inferior a 1 (um)")
    Integer numero,
    String bairro,
    
    @NotBlank(message = "O campo cidade não pode ser vazio")
    @NotNull(message = "O campo cidade não pode ser nulo")
    @Size(min = 3, message = "O campo cidade deve conter pelo menos 3 caracteres")
    String cidade,
    
    @NotBlank(message = "O campo estado não pode ser vazio")
    @NotNull(message = "O campo estado não pode ser nulo")
    @Size(min = 4, message = "O campo estado deve ter pelo menos 4 caracteres")
    String estado,
    
    @NotBlank(message = "A sigla federativa (UF) não pode ser vazia")
    @NotNull(message = "A sigla federativa (UF) não pode ser nula")
    @Size(min = 2, max = 2, message = "A sigla federativa (UF) deve conter 2 caracteres")
    String uf,
    
    @NotBlank(message = "O campo CEP não pode ser vazio e deve ser informado apenas os números")
    @NotNull(message = "O campo CEP não pode ser nulo e deve ser informado apenas os números")
    @Size(min = 8, max = 8, message = "O campo CEP deve conter 8 caracteres")
    String cep,

    String referencia,
    boolean principal
)
{

}
