package com.parceiroferramentas.api.parceiro_api.dto;

import com.parceiroferramentas.api.parceiro_api.enums.UF;

public record EnderecoDto(
    String logradouro,
    Integer numero,
    String bairro,
    String cidade,
    String estado,
    UF uf,
    String cep,
    String referencia,
    boolean principal
)
{

}
