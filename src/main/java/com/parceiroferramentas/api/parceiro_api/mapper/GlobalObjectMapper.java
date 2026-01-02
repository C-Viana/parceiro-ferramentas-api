package com.parceiroferramentas.api.parceiro_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.parceiroferramentas.api.parceiro_api.dto.FerramentaDto;
import com.parceiroferramentas.api.parceiro_api.dto.PermissaoRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.PermissaoResponseDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioResponseDto;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;

@Mapper(componentModel = "spring")
public interface GlobalObjectMapper {

    Ferramenta toEntity(FerramentaDto dto);

    @Mapping(target = "id", source = "id")
    FerramentaDto toDto(Ferramenta entidade);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "authorities", source = "authorities")
    Usuario toEntity(UsuarioResponseDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "password", source = "senha")
    @Mapping(target = "authorities", source = "authorities")
    @Mapping(target = "account_non_expired", constant = "true")
    @Mapping(target = "account_non_locked", constant = "true")
    @Mapping(target = "credentials_non_expired", constant = "true")
    Usuario toEntity(UsuarioRequestDto dto);

    @Mapping(target = "authorities", source = "authorities")
    UsuarioResponseDto toResponse(Usuario entidade);

    @Mapping(target = "authorities", source = "authorities")
    @Mapping(target = "senha", source = "password")
    UsuarioRequestDto toRequest(Usuario entidade);

    @Mapping(target = "authority", source = "authority")
    PermissaoRequestDto toRequest(Permissao permissao);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "authority", source = "authority")
    PermissaoResponseDto toResponse(Permissao permissao);
}
