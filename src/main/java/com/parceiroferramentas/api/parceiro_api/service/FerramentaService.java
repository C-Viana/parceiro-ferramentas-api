package com.parceiroferramentas.api.parceiro_api.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parceiroferramentas.api.parceiro_api.exception.InternalApplicationException;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FerramentaService {

    private final String BAD_REQUEST_MESSAGE = "Campos obrigatórios não podem ser nulos, vazios ou com preços menores ou iguais a zero";

    @Autowired
    private FerramentaRepository repository;

    public Ferramenta findById(Long id) {
        log.info("BUSCA PELA FERRAMENTA COM ID {"+id+"}");
        return repository.findById(id).orElse(null);
    }

    public Ferramenta fallbackCreateFerramentaenta(Ferramenta ferramenta, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO CRIAR NOVA FERRAMENTA", throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackCreateFerramentaenta")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackCreateFerramentaenta")
    public Ferramenta create(Ferramenta ferramenta) {
        log.info("CRIANDO NOVA FERRAMENTA: " + ferramenta.getNome());
        if(ferramenta.validateFields()==false) throw new InternalApplicationException(BAD_REQUEST_MESSAGE);
        LocalDate currenDate = LocalDate.now();
        ferramenta.setCriado_em(currenDate);
        ferramenta.setAtualizado_em(currenDate);
        return repository.save(ferramenta);
    }

    public void delete(Long id) {
        log.info("DELETANDO FERRAMENTA COM ID {"+id+"}");
        if(repository.findById(id)==null) return;
        repository.deleteById(id);
    }

    public Ferramenta fallbackUpdateFerramenta(Ferramenta ferramenta, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO ATUALIZAR NOVA FERRAMENTA", throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackUpdateFerramenta")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackUpdateFerramenta")
    public Ferramenta update(Ferramenta ferramenta) {
        log.info("ATUALIZANDO FERRAMENTA COM ID {"+ferramenta.getId()+"}");
        if(ferramenta.validateFields()==false) throw new InternalApplicationException(BAD_REQUEST_MESSAGE);
        LocalDate currenDate = LocalDate.now();
        ferramenta.setAtualizado_em(currenDate);
        return repository.save(ferramenta);
    }

    public Page<Ferramenta> findAll(Pageable pageable) {
        log.info("BUSCANDO TODAS AS FERRAMENTAS");
        return repository.findAll(pageable);
    }

    public Page<Ferramenta> findAllByType(String tipo, Pageable pageable) {
        if (tipo == null || tipo.isBlank()) {
            log.info("O PARÂMETRO \"tipo\" ESTÁ VAZIO. A BUSCA SERÁ FEITA POR TODAS AS FERRAMENTAS");
            return findAll(pageable);
        }
        log.info("BUSCANDO FERRAMENTAS DO TIPO: " + tipo);
        return repository.findByTipoEqualsIgnoreCase(tipo.trim(), pageable);
    }

    public Page<Ferramenta> findAllByName(String nome, Pageable pageable) {
        if (nome == null || nome.isBlank()) {
            log.info("O PARÂMETRO \"nome\" ESTÁ VAZIO. A BUSCA SERÁ FEITA POR TODAS AS FERRAMENTAS");
            return findAll(pageable);
        }
        log.info("BUSCANDO FERRAMENTAS DO NOME: " + nome);
        return repository.findByNomeContainsIgnoreCase(nome.trim(), pageable);
    }

}
