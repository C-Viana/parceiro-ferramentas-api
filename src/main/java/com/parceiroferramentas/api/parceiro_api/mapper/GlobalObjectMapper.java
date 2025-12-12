package com.parceiroferramentas.api.parceiro_api.mapper;

import org.mapstruct.Mapper;

import com.parceiroferramentas.api.parceiro_api.dto.FerramentaDto;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;

@Mapper(componentModel = "spring")
public interface GlobalObjectMapper {
    Ferramenta convert(FerramentaDto dto);
    FerramentaDto convert(Ferramenta entidade);
}
