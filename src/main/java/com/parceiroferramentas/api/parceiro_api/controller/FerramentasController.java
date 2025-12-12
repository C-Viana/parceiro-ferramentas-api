package com.parceiroferramentas.api.parceiro_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parceiroferramentas.api.parceiro_api.dto.FerramentaDto;
import com.parceiroferramentas.api.parceiro_api.model.DataConverter;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.service.FerramentaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/ferramentas")
public class FerramentasController {

    private Logger logger = LoggerFactory.getLogger(FerramentasController.class.getName());

    @Autowired
    private FerramentaService service;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<FerramentaDto> findById(@PathVariable Integer id) {
        Ferramenta entity = service.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DataConverter.convert(entity, FerramentaDto.class));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<FerramentaDto> create(@RequestBody FerramentaDto ferramenta) {
        Ferramenta entity = DataConverter.convert(ferramenta, Ferramenta.class);
        ferramenta.setId(service.create(entity).getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ferramenta);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}",consumes = "application/json" , produces = "application/json")
    public ResponseEntity<FerramentaDto> update(@PathVariable Integer id, @RequestBody FerramentaDto ferramenta) {
        FerramentaDto entity = findById(id).getBody();

        ferramenta.setId(id);
        ferramenta.setCriado_em(entity.getCriado_em());
        ferramenta.setAtualizado_em(entity.getAtualizado_em());

        if(!entity.equals(ferramenta)){
            logger.info("Alterando dados da ferramenta ID ["+id+"]");
            ferramenta = DataConverter.convert(
                service.update(DataConverter.convert(ferramenta, Ferramenta.class)),
                FerramentaDto.class
            );
        }
        else {
            logger.info("Nenhum dado alterado para a ferramenta ID ["+id+"]. Objeto novo é igual ao existente");
        }
        
        return ResponseEntity.ok().body(ferramenta);
    }

    /*
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FerramentaDto>> findAll() {
        List<Ferramenta> ferramentas = service.findAll();
        logger.info("Total de ferramentas encontradas: " + ferramentas.size());
        return ResponseEntity.ok(DataConverter.convert(ferramentas, FerramentaDto.class));
    }
    */

    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<FerramentaDto>> findAll(
            @RequestParam(value = "indice", defaultValue = "0") Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") String sort) {
        
        var sortOption = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
        Page<Ferramenta> ferramentas = service.findAll(PageRequest.of(page, size, Sort.by(sortOption, "id")));
        logger.info("Total de ferramentas encontradas: " + ferramentas.getTotalElements());
        return ResponseEntity.ok(ferramentas.map(ferramenta -> DataConverter.convert(ferramenta, FerramentaDto.class)));
    }

    @GetMapping(value = "/tipo", produces = "application/json")
    public ResponseEntity<Page<FerramentaDto>> findAllByType(
            @RequestParam String tipo, 
            @RequestParam(value = "indice", defaultValue = "0") Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") String sort) {
        var sortOption = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
        Page<Ferramenta> ferramentas = service.findAllByType(tipo, PageRequest.of(page, size, Sort.by(sortOption, "id")));
        logger.info("Total de ferramentas do tipo " + tipo + " encontradas: " + ferramentas.getTotalElements());
        return ResponseEntity.ok(ferramentas.map(ferramenta -> DataConverter.convert(ferramenta, FerramentaDto.class)));
    }

}
