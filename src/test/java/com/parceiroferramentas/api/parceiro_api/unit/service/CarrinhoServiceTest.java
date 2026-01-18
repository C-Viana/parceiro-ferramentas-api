package com.parceiroferramentas.api.parceiro_api.unit.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoRequestDto;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.CarrinhoService;

@ExtendWith(MockitoExtension.class)
public class CarrinhoServiceTest {

    @Mock private CarrinhoRepository repository;
    @Mock private UsuarioRepository usuarioRepo;
    @Mock private FerramentaRepository ferramentaRepo;

    @InjectMocks private CarrinhoService service;

    static Usuario usuario;
    static List<Ferramenta> ferramentas;
    static List<ItemCarrinho> carrinho;

    @BeforeAll
    public static void setup() {
        usuario = CreateMockedData.getInstance().getUsuarios().get(3);
        ferramentas = CreateMockedData.getInstance().getFerramentas();
    }

    @Test
    @DisplayName("Deve recuperar carrinho do usuário")
    void recuperarCarrinhoTeste() {
        usuario.setId(4L);
        int tamanhoCarrinho = 3;
        carrinho = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas);

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(repository.findItemCarrinhoByUsuarioId(usuario.getId())).thenReturn(carrinho);

        List<ItemCarrinho> response = service.recuperarCarrinho(usuario.getUsername());

        verify(repository, times(1)).findItemCarrinhoByUsuarioId(usuario.getId());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(usuario, response.get(0).getUsuario());
        Assertions.assertEquals(tamanhoCarrinho, response.size());

    }

    @Test
    @DisplayName("Deve salvar um item no carrinho")
    void salvarItemTeste() {
        usuario.setId(4L);
        int tamanhoCarrinho = 1;
        int quantidadeDoItem = 2;
        
        ItemCarrinho item = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas).getFirst();
        item.setId(99L);
        item.setQuantidade(quantidadeDoItem);

        Ferramenta ferramenta = item.getFerramenta();
        ferramenta.setId(1L);

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(ferramentaRepo.findById(ferramenta.getId())).thenReturn(Optional.of(ferramenta));
        Mockito.when(repository.save(Mockito.any(ItemCarrinho.class))).thenReturn(item);

        ItemCarrinho response = service.salvarItem(usuario.getUsername(), ferramentas.getFirst().getId(), quantidadeDoItem);

        verify(repository, times(1)).save(Mockito.any(ItemCarrinho.class));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(usuario, response.getUsuario());
        Assertions.assertEquals(quantidadeDoItem, response.getQuantidade());
        Assertions.assertEquals(item.getFerramenta().getNome(), response.getFerramenta().getNome());
    }

    @Test
    @DisplayName("Deve salvar vários itens no carrinho")
    void salvarTodosTeste() {
        usuario.setId(4L);
        int tamanhoCarrinho = 3;
        carrinho = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas);
        carrinho.get(1).setQuantidade(2);
        carrinho.get(2).setQuantidade(3);

        List<ItemCarrinhoRequestDto> resquestItens = CreateMockedData.getInstance().getItemCarrinhoRequest();

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(ferramentaRepo.findById(Mockito.any(Long.class)))
            .thenReturn(Optional.of(ferramentas.getFirst()));
        Mockito.when(repository.saveAll(Mockito.anyList())).thenReturn(carrinho);

        List<ItemCarrinho> response = service.salvarTodos(usuario.getUsername(), resquestItens);

        verify(repository, times(1)).saveAll(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(usuario, response.get(0).getUsuario());
        Assertions.assertEquals(tamanhoCarrinho, response.size());
        Assertions.assertEquals(carrinho.get(0).getQuantidade(), response.get(0).getQuantidade());
        Assertions.assertEquals(carrinho.get(1).getQuantidade(), response.get(1).getQuantidade());
        Assertions.assertEquals(carrinho.get(2).getQuantidade(), response.get(2).getQuantidade());
    }

    @Test
    @DisplayName("Deve remover um item do carrinho")
    void removerItemTeste() {
        int tamanhoCarrinho = 1;
        int quantidadeDoItem = 2;
        usuario.setId(4L);
        usuario.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas)));
        
        ItemCarrinho item = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas).getFirst();
        item.setId(99L);
        item.setQuantidade(quantidadeDoItem);

        Ferramenta ferramenta = item.getFerramenta();
        ferramenta.setId(1L);

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(ferramentaRepo.findById(ferramenta.getId())).thenReturn(Optional.of(ferramenta));
        Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(item));

        service.removerItem(usuario.getUsername(), item.getId());

        verify(repository, times(1)).delete(Mockito.any(ItemCarrinho.class));
    }

    @Test
    @DisplayName("Deve remover todos itens do carrinho")
    void removerTodosTeste() {
        int tamanhoCarrinho = 3;
        usuario.setId(4L);
        usuario.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas)));
        carrinho = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, usuario, ferramentas);

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(repository.findItemCarrinhoByUsuarioId(usuario.getId())).thenReturn(carrinho);

        service.removerTodos(usuario.getUsername());

        verify(repository, times(1)).deleteAll(carrinho);
    }

}
