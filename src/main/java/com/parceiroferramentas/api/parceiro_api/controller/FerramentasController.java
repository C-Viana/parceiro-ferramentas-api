package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parceiroferramentas.api.parceiro_api.controller.openapi.FerramentasDocumentation;
import com.parceiroferramentas.api.parceiro_api.dto.FerramentaDto;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.service.FerramentaService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Validated
@RequestMapping("/api/v1/ferramentas")
public class FerramentasController implements FerramentasDocumentation {

    private Logger logger = LoggerFactory.getLogger(FerramentasController.class.getName());

    @Autowired
    private FerramentaService service;

    @Autowired
    private GlobalObjectMapper mapper;

    @Override
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<FerramentaDto> findById(@PathVariable @Min(0) Long id) {
        Ferramenta entity = service.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PostMapping(consumes = "application/json")
    public ResponseEntity<FerramentaDto> create(@RequestHeader("Authorization") String token, @RequestBody FerramentaDto ferramenta) {
        Ferramenta entity = service.create(mapper.toEntity(ferramenta));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(entity.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(mapper.toDto(entity));
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(value = "/{id}",consumes = "application/json" , produces = "application/json")
    public ResponseEntity<FerramentaDto> update(@PathVariable Long id, @RequestBody FerramentaDto ferramenta) {
        FerramentaDto entity = findById(id).getBody();

        //ferramenta.setId(id);
        //ferramenta.setCriado_em(entity.getCriado_em());

        if(entity.equals(ferramenta)){
            logger.info("Nenhum dado alterado para a ferramenta ID ["+id+"]. Objeto novo é igual ao existente");
        }
        else {
            logger.info("Alterando dados da ferramenta ID ["+id+"]");
            ferramenta = mapper.toDto(
                service.update(mapper.toEntity(ferramenta))
            );
        }
        
        return ResponseEntity.ok().body(ferramenta);
    }

    @Override
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<FerramentaDto>> findAll(
            @RequestParam(value = "indice", defaultValue = "0") Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") String sort) {
        
        var sortOption = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
        Page<Ferramenta> ferramentas = service.findAll(PageRequest.of(page, size, Sort.by(sortOption, "id")));
        logger.info("Total de ferramentas encontradas: " + ferramentas.getTotalElements());
        return ResponseEntity.ok(ferramentas.map(ferramenta -> mapper.toDto(ferramenta)));
    }

    @Override
    @GetMapping(value = "/tipo", produces = "application/json")
    public ResponseEntity<Page<FerramentaDto>> findAllByType(
            @RequestParam String tipo, 
            @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24) Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") @Pattern(regexp="asc|desc") String sort) {
        var sortOption = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
        Page<Ferramenta> ferramentas = service.findAllByType(tipo, PageRequest.of(page, size, Sort.by(sortOption, "id")));
        logger.info("Total de ferramentas do tipo " + tipo + " encontradas: " + ferramentas.getTotalElements());
        return ResponseEntity.ok(ferramentas.map(ferramenta -> mapper.toDto(ferramenta)));
    }

}
