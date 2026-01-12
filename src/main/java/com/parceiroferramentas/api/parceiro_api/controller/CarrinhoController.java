package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoDto;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoRequestDto;
import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.service.CarrinhoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping(value = "/api/v1/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService service;

    @Autowired
    private GlobalObjectMapper mapper;

    @Autowired
    private JwtTokenService tokenService;

    private Logger logger = LoggerFactory.getLogger(EnderecoController.class);

    private String extrairUsername(String jwtAccessToken) {
        if(jwtAccessToken == null || !jwtAccessToken.startsWith("Bearer")) {
            throw new InvalidAuthorizationException("O token informado está nulo ou é inválido");
        }
        return tokenService.decodeToken(jwtAccessToken.split(" ")[1]).getSubject();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ItemCarrinhoDto>> buscaCarrinhoUsuario(@PathVariable Long usuarioId) {
        logger.info("Buscando o carrinho do usuário de ID "+usuarioId);
        List<ItemCarrinho> carrinho = service.recuperarCarrinho(usuarioId);
        logger.info("Carrinho encontrado com total de " + carrinho.size() + " itens");
        List<ItemCarrinhoDto> carrinhoDto = carrinho.stream().map(item -> mapper.toItemCarrinhoDto(item)).toList();
        return ResponseEntity.ok(carrinhoDto);
    }

    @PostMapping
    public ResponseEntity<ItemCarrinhoDto> adicionarItem(@RequestHeader("Authorization") String token, @RequestBody ItemCarrinhoRequestDto item) {
        if(item == null 
            || item.ferramenta_id() == null
            || item.ferramenta_id() < 1L
            || item.quantidade() == null
            || item.quantidade() < 1
        ) throw new BadRequestException("As informações da ferramenta não são válidas ["+item.toString()+"]");
        
        String username = extrairUsername(token);
        logger.info("Salvando item no carrinho do usuário "+username);
        
        ItemCarrinho itemSalvo = service.salvarItem(username, item.ferramenta_id(), item.quantidade());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.toItemCarrinhoDto(itemSalvo));
    }

    @PostMapping(value = "/todos")
    public ResponseEntity<List<ItemCarrinhoDto>> adicionarItens(@RequestHeader("Authorization") String token, @RequestBody List<ItemCarrinhoRequestDto> itens) {
        itens.forEach( item -> {
            if(item == null 
                || item.ferramenta_id() == null
                || item.ferramenta_id() < 1L
                || item.quantidade() == null
                || item.quantidade() < 1
            ) throw new BadRequestException("As informações da ferramenta não são válidas ["+item.toString()+"]");
        });
        
        String username = extrairUsername(token);
        logger.info("Salvando todos os itens no carrinho do usuário "+username);
        logger.info("Quantidade de itens recebidos "+itens.size());
        List<ItemCarrinho> itensSalvos = service.salvarTodos(username, itens);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itensSalvos.get(0).getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.toListaItemCarrinhoDto(itensSalvos));
    }

    @DeleteMapping(value = "/{itemCarrinhoId}")
    public ResponseEntity<Void> removerItem(@RequestHeader("Authorization") String token, @PathVariable Long itemCarrinhoId) {
        String username = extrairUsername(token);
        service.removerItem(username, itemCarrinhoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/limpar")
    public ResponseEntity<Void> limparCarrinho(@RequestHeader("Authorization") String token) {
        String username = extrairUsername(token);
        service.removerTodos(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ItemCarrinhoDto> atualizarItem(@RequestBody ItemCarrinhoDto itemAtualizado) {
        logger.info("ITEM DTO: " + itemAtualizado.toString());
        ItemCarrinho entidade = service.atualizarItem(mapper.toItemCarrinho(itemAtualizado));
        return ResponseEntity.ok(mapper.toItemCarrinhoDto(entidade));
    }
    
}
