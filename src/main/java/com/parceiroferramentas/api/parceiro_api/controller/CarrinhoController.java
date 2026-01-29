package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.controller.openapi.CarrinhoDocumentation;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoDto;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoRequestDto;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.service.CarrinhoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/carrinho")
public class CarrinhoController implements CarrinhoDocumentation {

    private final CarrinhoService service;
    private final GlobalObjectMapper mapper;
    private final JwtTokenService tokenService;

    private String extrairUsername(String jwtAccessToken) {
        if(jwtAccessToken == null || !jwtAccessToken.startsWith("Bearer")) {
            throw new InvalidAuthorizationException("O token informado está nulo ou é inválido");
        }
        return tokenService.decodeToken(jwtAccessToken.split(" ")[1]).getSubject();
    }

    @Override
    @GetMapping("/usuario")
    public ResponseEntity<List<ItemCarrinhoDto>> buscaCarrinhoUsuario(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token
    ) {
        String extractedUsername = extrairUsername(token);
        log.info("BUSCANDO O CARRINHO DO USUÁRIO DE ID "+extractedUsername);
        List<ItemCarrinho> carrinho = service.recuperarCarrinho(extractedUsername);
        log.info("CARRINHO ENCONTRADO COM TOTAL DE " + carrinho.size() + " ITENS");
        List<ItemCarrinhoDto> carrinhoDto = carrinho.stream().map(item -> mapper.toItemCarrinhoDto(item)).toList();
        return ResponseEntity.ok(carrinhoDto);
    }

    @Override
    @PostMapping
    public ResponseEntity<ItemCarrinhoDto> adicionarItem(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @Valid
        @RequestBody ItemCarrinhoRequestDto item
    ) {
        String username = extrairUsername(token);
        log.info("SALVANDO ITEM NO CARRINHO DO USUÁRIO "+username);
        
        ItemCarrinho itemSalvo = service.salvarItem(username, item.ferramenta_id(), item.quantidade());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.toItemCarrinhoDto(itemSalvo));
    }

    @Override
    @PostMapping(value = "/todos")
    public ResponseEntity<List<ItemCarrinhoDto>> adicionarItens(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @Valid
        @RequestBody List<ItemCarrinhoRequestDto> itens
    ) {
        String username = extrairUsername(token);
        log.info("SALVANDO ["+itens.size()+"] ITENS NO CARRINHO DO USUÁRIO "+username);
        List<ItemCarrinho> itensSalvos = service.salvarTodos(username, itens);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itensSalvos.get(0).getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.toListaItemCarrinhoDto(itensSalvos));
    }

    @Override
    @DeleteMapping(value = "/{itemCarrinhoId}")
    public ResponseEntity<Void> removerItem(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @PathVariable Long itemCarrinhoId
    ) {
        String username = extrairUsername(token);
        log.info("REMOVER ITEM DE CARRINHO DO USUARIO "+username);
        service.removerItem(username, itemCarrinhoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping(value = "/limpar")
    public ResponseEntity<Void> limparCarrinho(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token
    ) {
        String username = extrairUsername(token);
        log.info("LIMPAR CARRINHO DO USUARIO "+username);
        service.removerTodos(username);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping
    public ResponseEntity<ItemCarrinhoDto> atualizarItem(
        @Valid
        @RequestBody ItemCarrinhoDto itemAtualizado
    ) {
        log.info("ATUALIZAR ITEM DO CARRINHO");
        ItemCarrinho entidade = service.atualizarItem(mapper.toItemCarrinho(itemAtualizado));
        return ResponseEntity.ok(mapper.toItemCarrinhoDto(entidade));
    }
    
}
