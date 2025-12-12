package com.parceiroferramentas.api.parceiro_api.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.parceiroferramentas.api.parceiro_api.exception.InternalApplicationException;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;

@Service
public class FerramentaService {

    private Logger logger = LoggerFactory.getLogger(FerramentaService.class.getName());
    private final String BAD_REQUEST_MESSAGE = "Campos obrigatórios não podem ser nulos, vazios ou com preços menores ou iguais a zero";

    @Autowired
    private FerramentaRepository repository;

    public Ferramenta findById(Long id) {
        logger.info("Busca pela ferramenta com ID {"+id+"}");
        return repository.findById(id).orElse(null);
    }

    public Ferramenta create(Ferramenta ferramenta) {
        logger.info("Criando nova ferramenta: " + ferramenta.getNome());
        if(ferramenta.validateFields()==false) throw new InternalApplicationException(BAD_REQUEST_MESSAGE);
        LocalDate currenDate = LocalDate.now();
        ferramenta.setCriado_em(currenDate);
        ferramenta.setAtualizado_em(currenDate);
        return repository.save(ferramenta);
    }

    public void delete(Long id) {
        logger.info("Deletando ferramenta com ID {"+id+"}");
        if(repository.findById(id)==null) return;
        repository.deleteById(id);
    }

    public Ferramenta update(Ferramenta ferramenta) {
        logger.info("Atualizando ferramenta com ID {"+ferramenta.getId()+"}");
        if(ferramenta.validateFields()==false) throw new InternalApplicationException(BAD_REQUEST_MESSAGE);
        LocalDate currenDate = LocalDate.now();
        ferramenta.setAtualizado_em(currenDate);
        return repository.save(ferramenta);
    }

    /*
    public List<Ferramenta> findAll() {
        logger.info("Buscando todas as ferramentas");
        return repository.findAll();
    }
    */

    public Page<Ferramenta> findAll(Pageable pageable) {
        logger.info("Buscando todas as ferramentas");
        return repository.findAll(pageable);
    }

    public Page<Ferramenta> findAllByType(String tipo, Pageable pageable) {
        if (tipo == null || tipo.isBlank()) {
            logger.info("O parâmetro \"tipo\" está vazio. A busca será feita por todas as ferramentas.");
            return findAll(pageable);
        }
        logger.info("Buscando ferramentas do tipo: " + tipo);
        return repository.findByTipoEqualsIgnoreCase(tipo.trim(), pageable);
    }

}
