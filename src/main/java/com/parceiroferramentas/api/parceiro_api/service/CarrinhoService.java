package com.parceiroferramentas.api.parceiro_api.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoRequestDto;
import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.exception.NotFoundException;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private FerramentaRepository ferramentaRepo;

    public List<ItemCarrinho> recuperarCarrinho(String usuarioUsername) {
        Usuario usuario = buscaUsuario(usuarioUsername);
        return repository.findItemCarrinhoByUsuarioId(usuario.getId());
    }

    public ItemCarrinho fallbackSalvarItemCarrinho(String username, Long ferramentaId, Integer quantidade, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO SALVAR ITEM NO CARRINHO", throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackSalvarItemCarrinho")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackSalvarItemCarrinho")
    public ItemCarrinho salvarItem(String username, Long ferramentaId, Integer quantidade) {
        Usuario usuario = buscaUsuario(username);
        Ferramenta ferramenta = buscaFerramenta(ferramentaId);
        ItemCarrinho item = new ItemCarrinho();
        item.setUsuario(usuario);
        item.setFerramenta(ferramenta);
        item.setQuantidade(quantidade);
        item.setPrecoAluguelMomento(BigDecimal.valueOf(ferramenta.getPreco_aluguel()));
        item.setPrecoVendaMomento(BigDecimal.valueOf(ferramenta.getPreco_venda()));
        item.setUrlImage(ferramenta.getLista_imagens().get(0));
        return repository.save(item);
    }

    public List<ItemCarrinho> fallbackSalvarTodosCarrinho(String username, List<ItemCarrinhoRequestDto> itens, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO SALVAR LISTA DE ITENS NO CARRINHO", throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackSalvarTodosCarrinho")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackSalvarTodosCarrinho")
    public List<ItemCarrinho> salvarTodos(String username, List<ItemCarrinhoRequestDto> itens) {
        Usuario usuario = buscaUsuario(username);
        List<ItemCarrinho> ferramentas = new ArrayList<>();
        itens.forEach( itemDto -> {
            Ferramenta f = buscaFerramenta(itemDto.ferramenta_id());
            ItemCarrinho item = new ItemCarrinho();
            item.setUsuario(usuario);
            item.setFerramenta(f);
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoAluguelMomento(BigDecimal.valueOf(f.getPreco_aluguel()));
            item.setPrecoVendaMomento(BigDecimal.valueOf(f.getPreco_venda()));
            item.setUrlImage(f.getLista_imagens().get(0));
            ferramentas.add(item);
        });
        return repository.saveAll(ferramentas);
    }

    public void removerItem(String username, Long itemCarrinhoId) {
        Usuario usuario = buscaUsuario(username);
        ItemCarrinho item = repository.findById(itemCarrinhoId).orElse(null);
        if( usuario.getId() != item.getUsuario().getId() ) throw new RuntimeException("Operação não permitida");
        if( item != null )
            repository.delete(item);
        usuario.removerDoCarrinho(buscaFerramenta(item.getFerramenta().getId()));
    }

    public void removerTodos(String username) {
        Usuario usuario = buscaUsuario(username);
        List<ItemCarrinho> itens = repository.findItemCarrinhoByUsuarioId(usuario.getId());
        if( usuario.getId() != itens.get(0).getUsuario().getId() ) throw new RuntimeException("Operação não permitida");
        if( itens != null )
            repository.deleteAll(itens);
        usuario.limparCarrinho();
    }

    public ItemCarrinho fallbackAtualizarItemCarrinho(ItemCarrinho itemAtual, Throwable throwable) {
        log.error("CIRCUIT BREAKER: ERRO AO ATUALIZAR O ITEM DO CARRINHO "+itemAtual.getId(), throwable);
        throw new RuntimeException("Serviço indisponível no momento. Tente novamente mais tarde.");
    }

    @CircuitBreaker(name = "backendGlobalBreaker", fallbackMethod = "fallbackAtualizarItemCarrinho")
    @Retry(name = "backendGlobalRetry", fallbackMethod = "fallbackAtualizarItemCarrinho")
    public ItemCarrinho atualizarItem(ItemCarrinho itemAtual) {
        if(itemAtual == null || itemAtual.getId() == null) throw new BadRequestException("Dados do item são inválidos ou estão ausentes");
        ItemCarrinho itemAtualizavel = repository.findById(itemAtual.getId()).orElse(null);
        if(itemAtualizavel == null) throw new NotFoundException("O item informado não foi encontrado");
        atualizaItemCarrinho(itemAtualizavel, itemAtual);
        return repository.save(itemAtualizavel);
    }

    private void atualizaItemCarrinho(ItemCarrinho anterior, ItemCarrinho atual) {
        anterior.setQuantidade(atual.getQuantidade());
        anterior.setPrecoVendaMomento(atual.getPrecoVendaMomento());
        anterior.setPrecoAluguelMomento(atual.getPrecoAluguelMomento());
        anterior.setDataAdicao(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));
    }

    private Usuario buscaUsuario(String username) {
        Usuario usuario = usuarioRepo.findUsuarioByUsername(username);
        if(usuario == null) throw new NotFoundException("O usuário informado ["+username+"] não foi encontrado");
        return usuario;
    }

    private Ferramenta buscaFerramenta(Long ferramentaId) {
        Ferramenta resposta = ferramentaRepo.findById(ferramentaId).orElse(null);
        if(resposta == null) throw new NotFoundException("A ferramenta informada [ID "+ferramentaId+"] não foi encontrada");
        return resposta;
    }

}
