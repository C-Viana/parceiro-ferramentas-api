package com.parceiroferramentas.api.parceiro_api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parceiroferramentas.api.parceiro_api.exception.NotFoundException;
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.repository.CompradorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CompradorService {

    private final CompradorRepository repository;

    public Comprador findById(UUID id) {
        log.info("BUSCA PELO COMPRADOR COM ID {"+id+"}");
        return repository.findById(id).orElse(null);
    }

    public Comprador findByCpf(String documento) {
        log.info("BUSCA PELO COMPRADOR COM documento {"+documento+"}");
        return repository.findCompradorByDocumento(documento).orElse(null);
    }

    public Comprador create(Comprador comprador) {
        log.info("CADASTRANDO NOVO COMPRADOR COM documento {"+comprador.getDocumento()+"}");
        return repository.save(comprador);
    }

    public Comprador update(Comprador compradorAtualizado) {
        log.info("DELETANDO CADASTRO DO COMPRADOR COM documento {"+compradorAtualizado.getDocumento()+"}");
        Comprador foundEntity = repository.findCompradorByDocumento(compradorAtualizado.getDocumento()).orElse(null);
        if(foundEntity==null) throw new NotFoundException("Nenhum cadastro encontrado com o documento "+compradorAtualizado.getDocumento());
        return repository.save(compradorAtualizado);
    }

    public void delete(String documento) {
        log.info("DELETANDO CADASTRO DO COMPRADOR COM documento {"+documento+"}");
        Comprador foundEntity = repository.findCompradorByDocumento(documento).orElse(null);
        if(foundEntity==null) return;
        repository.delete(foundEntity);
    }

    public boolean validarAtualizacaoComprador(Comprador original, Comprador atualizado) {
        return (
            atualizado.getNascimento() == null ||
            atualizado.getEnderecos().size() < 1 ||
            atualizado.getEmail() == null ||  atualizado.getEmail().isBlank() ||
            atualizado.getNome() == null || atualizado.getNome().isBlank() ||
            atualizado.getPhone() == null || atualizado.getPhone().isBlank()
        );
    }
}
