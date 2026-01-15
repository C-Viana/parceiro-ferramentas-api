package com.parceiroferramentas.api.parceiro_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.parceiroferramentas.api.parceiro_api.dto.EnderecoDto;
import com.parceiroferramentas.api.parceiro_api.dto.FerramentaDto;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoDto;
import com.parceiroferramentas.api.parceiro_api.dto.PagamentoRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.PedidoResponseDto;
import com.parceiroferramentas.api.parceiro_api.dto.PermissaoRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.PermissaoResponseDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioResponseDto;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.Pagamento;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;

@Mapper(componentModel = "spring")
public interface GlobalObjectMapper {

    Ferramenta toFerramentaEntity(FerramentaDto dto);

    @Mapping(target = "id", source = "id")
    FerramentaDto toFerramentaDto(Ferramenta entidade);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "authorities", source = "authorities")
    Usuario toUsuarioEntity(UsuarioResponseDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "password", source = "senha")
    @Mapping(target = "authorities", source = "authorities")
    @Mapping(target = "account_non_expired", constant = "true")
    @Mapping(target = "account_non_locked", constant = "true")
    @Mapping(target = "credentials_non_expired", constant = "true")
    Usuario toUsuarioEntity(UsuarioRequestDto dto);

    @Mapping(target = "authorities", source = "authorities")
    UsuarioResponseDto toUsuarioResponseDto(Usuario entidade);

    @Mapping(target = "authorities", source = "authorities")
    @Mapping(target = "senha", source = "password")
    UsuarioRequestDto toUsuarioRequestDto(Usuario entidade);

    @Mapping(target = "authority", source = "authority")
    PermissaoRequestDto toPermissaoRequestDto(Permissao permissao);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "authority", source = "authority")
    PermissaoResponseDto toPermissaoResponseDto(Permissao permissao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", expression = "java(com.parceiroferramentas.api.parceiro_api.enums.ESTADOS.getByDisplayValue(dto.estado()))")
    Endereco toEnderecoEntity(EnderecoDto dto);

    @Mapping(target = "estado", expression = "java(entity.getEstadoAsString())")
    EnderecoDto toEnderecoDto(Endereco entity);

    ItemCarrinho toItemCarrinho(ItemCarrinhoDto dto);
    ItemCarrinhoDto toItemCarrinhoDto(ItemCarrinho entidade);
    List<ItemCarrinho> toListaItemCarrinho(List<ItemCarrinhoDto> dto);
    List<ItemCarrinhoDto> toListaItemCarrinhoDto(List<ItemCarrinho> entidade);

    PedidoResponseDto toPedidoResponseDto(Pedido pedido);

    // @Mapping(target = "detalhes", expression = "java(detalhes().toString())")
    // @Mapping(target = "formaPagamento", expression = "java(TIPO_PAGAMENTO.getByDisplayValue(formaPagamento()))")
    // Pagamento toPagamento(PagamentoRequestDto pagamentoDto);

}
