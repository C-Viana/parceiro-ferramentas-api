package com.parceiroferramentas.api.parceiro_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.exception.NotFoundException;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private FerramentaRepository ferramentaRepo;

    public List<ItemCarrinho> recuperarCarrinho(Long usuarioId) {
        Usuario usuario = buscaUsuario(usuarioId);
        return repository.findItemCarrinhoByUsuarioId(usuario.getId());
    }

    public ItemCarrinho salvarItem(String username, Long ferramentaId, Integer quantidade) {
        Usuario usuario = buscaUsuario(username);
        Ferramenta ferramenta = buscaFerramenta(ferramentaId);
        ItemCarrinho item = new ItemCarrinho();
        item.setUsuario(usuario);
        item.setFerramenta(ferramenta);
        item.setQuantidade(quantidade);
        item.setPrecoAluguelMomento(BigDecimal.valueOf(ferramenta.getPreco_aluguel()));
        item.setPrecoVendaMomento(BigDecimal.valueOf(ferramenta.getPreco_venda()));
        return repository.save(item);
    }

    public List<ItemCarrinho> salvarTodos(List<ItemCarrinho> itens) {
        return repository.saveAll(itens);
    }

    public void removerItem(ItemCarrinho item) {
        repository.delete(item);
    }

    public void removerTodos(List<ItemCarrinho> itens) {
        repository.deleteAll(itens);
    }

    public ItemCarrinho atualizarItem(ItemCarrinho itemAtual) {
        if(!itemCarrinhoValido(itemAtual)) throw new BadRequestException("Algum dos dados informados está inválido");
        ItemCarrinho itemAtualizavel = repository.findById(itemAtual.getId()).orElse(null);
        if(itemAtualizavel == null) throw new NotFoundException("O item informado não foi encontrado");
        atualizaItemCarrinho(itemAtualizavel, itemAtual);
        return repository.save(itemAtualizavel);
    }

    public List<ItemCarrinho> atualizarTodos(List<ItemCarrinho> itens) {
        ItemCarrinho itemAtualizavel = null;
        List<ItemCarrinho> itensAtualizados = new ArrayList<>();
        for (int i = 0; i < itens.size(); i++) {
            if(!itemCarrinhoValido(itens.get(i))) throw new BadRequestException("Os dados do item ID ["+itens.get(i).getId()+"] estão inválidos");
            itemAtualizavel = repository.findById(itens.get(i).getId()).orElse(null);
            if(itemAtualizavel == null) throw new NotFoundException("O item informado não foi encontrado");
            atualizaItemCarrinho(itemAtualizavel, itens.get(i));
            itensAtualizados.add(itemAtualizavel);
        }
        return repository.saveAll(itensAtualizados);
    }

    private boolean itemCarrinhoValido(ItemCarrinho item) {
        boolean ferramentaNula = item.getFerramenta() != null;
        boolean ferramentaExiste = ferramentaRepo.existsById(item.getFerramenta().getId());
        boolean precoCompraValido = item.getPrecoVendaMomento() != null && item.getPrecoVendaMomento().compareTo(new BigDecimal("0.0")) > 0;
        boolean precoAluguelValido = item.getPrecoAluguelMomento() != null && item.getPrecoAluguelMomento().compareTo(new BigDecimal("0.0")) > 0;
        boolean dataValida = item.getDataAdicao() != null;
        return (ferramentaNula && ferramentaExiste && precoCompraValido && precoAluguelValido && dataValida);
    }

    private void atualizaItemCarrinho(ItemCarrinho anterior, ItemCarrinho atual) {
        anterior.setQuantidade(atual.getQuantidade());
        anterior.setPrecoVendaMomento(atual.getPrecoVendaMomento());
        anterior.setPrecoAluguelMomento(atual.getPrecoAluguelMomento());
        anterior.setDataAdicao(atual.getDataAdicao());
    }

    private Usuario buscaUsuario(String username) {
        Usuario usuario = usuarioRepo.findUsuarioByUsername(username);
        if(usuario == null) throw new NotFoundException("O usuário informado ["+username+"] não foi encontrado");
        return usuario;
    }

    private Usuario buscaUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElse(null);
        if(usuario == null) throw new NotFoundException("O usuário informado [ID "+usuarioId+"] não foi encontrado");
        return usuario;
    }

    private Ferramenta buscaFerramenta(Long ferramentaId) {
        Ferramenta resposta = ferramentaRepo.findById(ferramentaId).orElse(null);
        if(resposta == null) throw new NotFoundException("A ferramenta informada [ID "+ferramentaId+"] não foi encontrada");
        return resposta;
    }

}
