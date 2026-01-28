package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parceiroferramentas.api.parceiro_api.controller.openapi.EnderecoDocumentation;
import com.parceiroferramentas.api.parceiro_api.dto.EnderecoDto;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.service.EnderecoService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@Slf4j
@Validated
@RequestMapping(value = "/api/v1/endereco")
public class EnderecoController implements EnderecoDocumentation{

    @Autowired
    private EnderecoService service;

    @Autowired
    private GlobalObjectMapper mapper;

    @Override
    @GetMapping(value = "/todos")
    public ResponseEntity<Page<EnderecoDto>> getEnderecos( 
            @RequestParam(value = "indice", defaultValue = "0") Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") String sort
        ) {
        var sortOption = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
        log.info("BUSCANDO TODOS OS ENDEREÇOS");
        Page<Endereco> entidade = service.findAll( PageRequest.of(page, size, Sort.by(sortOption, "id")) );
        log.info("TOTAL DE ENDEREÇOS ENCONTRADOS: {}", entidade.getTotalElements());
        return ResponseEntity.ok( entidade.map(item -> mapper.toEnderecoDto(item)) );
    }

    @Override
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EnderecoDto> getEnderecoById(@PathVariable Long id) {
        log.info("BUSCANDO O REGISTRO DE ENDEREÇO COM ID {}", id);
        Endereco entidade = service.findById(id);
        if(entidade == null) return ResponseEntity.noContent().build();
        EnderecoDto dto = mapper.toEnderecoDto(entidade);
        return ResponseEntity.ok( dto );
    }

    @Override
    @GetMapping(value = "/usuario/{usuarioId}", produces = "application/json")
    public ResponseEntity<List<EnderecoDto>> findEnderecosDoUsuario(@PathVariable Long usuarioId) {
        log.info("BUSCANDO O REGISTRO DE ENDEREÇO COM ID {}", usuarioId);
        List<Endereco> entidade = service.findUsuarioEnderecos(usuarioId);
        if(entidade.size() < 1) return ResponseEntity.noContent().build();
        List<EnderecoDto> dto = entidade.stream().map(item -> mapper.toEnderecoDto(item)).toList();
        return ResponseEntity.ok( dto );
    }

    @Override
    @PostMapping(value = "/usuario/{userId}")
    public ResponseEntity<EnderecoDto> cadastrarEndereco(
        @PathVariable Long userId, 
        @Valid
        @RequestBody EnderecoDto dto
    ) {
        log.info("CADASTRAR NOVO ENDEREÇO PARA O USUÁRIO {}", userId);
        Endereco response = service.create(userId, mapper.toEnderecoEntity(dto));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created( uri ).body(mapper.toEnderecoDto(response));
    }

    @Override
    @PutMapping(value = "/{id}")
    public ResponseEntity<EnderecoDto> atualizarEndereco(
        @PathVariable Long id, 
        @Valid
        @RequestBody EnderecoDto dto
    ) {
        log.info("ATUALIZAR O ENDEREÇO DE ID {}", id);
        Endereco response = service.update(id, mapper.toEnderecoEntity(dto));
        return ResponseEntity.ok( mapper.toEnderecoDto(response) );
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removerEndereco(@PathVariable Long id) {
        log.info("REMOVER O ENDEREÇO DE ID {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
