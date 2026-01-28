package com.parceiroferramentas.api.parceiro_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.exception.NotFoundException;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.EnderecoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Page<Endereco> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Endereco findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Endereco> findUsuarioEnderecos(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if(usuario == null) throw new BadRequestException("Dados de usuario não informados para buscar endereços");
        return repository.findEnderecoByUsuarioId(usuario.getId());
    }

    public Endereco fallbackCreateEndereco(Long usuarioId, Endereco novoEndereco, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO CRIAR NOVO ENDERECO", throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackCreateEndereco")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackCreateEndereco")
    public Endereco create(Long usuarioId, Endereco novoEndereco) {
        Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
        if( u == null ) throw new BadRequestException("Nenhum usuário encontrado pelo ID ["+usuarioId+"]");

        List<Endereco> enderecos = repository.findEnderecoByUsuarioId(usuarioId);
        if(enderecos.size() > 0) {
            if(novoEndereco.isPrincipal() == true){
                enderecos.stream().forEach(e -> e.setPrincipal(false));
                repository.saveAll(enderecos);
                novoEndereco.setPrincipal(true);
            }
            else
                novoEndereco.setPrincipal(false);
        }
        else
            novoEndereco.setPrincipal(true);
        
        novoEndereco.setUsuario(u);
        return repository.save(novoEndereco);
    }

    public Endereco fallbackUpdateEndereco(Long enderecoId, Endereco enderecoAtualizado, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO ATUALIZAR O ENDERECO "+enderecoId, throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackUpdateEndereco")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackUpdateEndereco")
    public Endereco update(Long enderecoId, Endereco enderecoAtualizado) {
        Endereco entidade = repository.findById(enderecoId).orElse(null);
        if(entidade == null) throw new NotFoundException("Nenhum endereço encontrado para o id ["+enderecoId+"]");
        entidade.setLogradouro(enderecoAtualizado.getLogradouro());
        entidade.setNumero(enderecoAtualizado.getNumero());
        entidade.setBairro(enderecoAtualizado.getBairro());
        entidade.setCidade(enderecoAtualizado.getCidade());
        entidade.setEstado(enderecoAtualizado.getEstado());
        entidade.setUf(enderecoAtualizado.getUf());
        entidade.setCep(enderecoAtualizado.getCep());
        entidade.setReferencia(enderecoAtualizado.getReferencia());

        if(entidade.enderecoInvalido()) throw new BadRequestException("Um ou mais campos estão inválidos. Verifique se há inconsistências e tente novamente.");

        if(entidade.isPrincipal() == false && enderecoAtualizado.isPrincipal() == true) {
            List<Endereco> enderecos = repository.findEnderecoByUsuarioId(entidade.getUsuario().getId());
            if(enderecos.size() > 1) {
                enderecos.stream().forEach(e -> e.setPrincipal(false));
                repository.saveAll(enderecos);
                entidade.setPrincipal(true);
            }
            else if(entidade.isPrincipal() == true && enderecoAtualizado.isPrincipal() == false) {
                throw new BadRequestException("Para alterar o endereço principal é necessário ter dois ou mais endereços cadastrados");
            }
        }
        else
            entidade.setPrincipal(enderecoAtualizado.isPrincipal());
        return repository.save(entidade);
    }

    public void delete(Long enderecoId) {
        Endereco entidade = repository.findById(enderecoId).orElse(null);
        if(entidade != null) repository.delete(entidade);
    }
}
