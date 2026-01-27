package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@Validated
@RequestMapping(value = "/api/v1/endereco")
public class EnderecoController implements EnderecoDocumentation{

    @Autowired
    private EnderecoService service;

    @Autowired
    private GlobalObjectMapper mapper;

    private Logger logger = LoggerFactory.getLogger(EnderecoController.class);

    @Override
    @GetMapping(value = "/todos")
    public ResponseEntity<Page<EnderecoDto>> getEnderecos( 
            @RequestParam(value = "indice", defaultValue = "0") Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") String sort
        ) {
        var sortOption = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
        logger.info("Buscando todos os endereços");
        Page<Endereco> entidade = service.findAll( PageRequest.of(page, size, Sort.by(sortOption, "id")) );
        logger.info("Total de endereços encontrados: {}", entidade.getTotalElements());
        return ResponseEntity.ok( entidade.map(item -> mapper.toEnderecoDto(item)) );
    }

    @Override
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EnderecoDto> getEnderecoById(@PathVariable Long id) {
        logger.info("Buscando o registro de endereço com ID {}", id);
        Endereco entidade = service.findById(id);
        if(entidade == null) return ResponseEntity.noContent().build();
        EnderecoDto dto = mapper.toEnderecoDto(entidade);
        return ResponseEntity.ok( dto );
    }

    @Override
    @GetMapping(value = "/usuario/{usuarioId}", produces = "application/json")
    public ResponseEntity<List<EnderecoDto>> findEnderecosDoUsuario(@PathVariable Long usuarioId) {
        logger.info("Buscando o registro de endereço com ID {}", usuarioId);
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
        logger.info("Cadastrar novo endereço para o usuário {}", userId);
        logger.info("PAYLOAD {}", dto);
        Endereco response = service.create(userId, mapper.toEnderecoEntity(dto));
        logger.info("ENDEREÇO CADASTRADO COM ID {}", response.getId());

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
        logger.info("Atualizar o endereço de ID {}", id);
        logger.info("PAYLOAD {}", dto);
        Endereco response = service.update(id, mapper.toEnderecoEntity(dto));
        return ResponseEntity.ok( mapper.toEnderecoDto(response) );
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removerEndereco(@PathVariable Long id) {
        logger.info("Remover o endereço de ID {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
