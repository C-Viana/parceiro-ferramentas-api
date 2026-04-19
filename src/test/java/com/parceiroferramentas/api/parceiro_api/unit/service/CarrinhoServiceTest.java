package com.parceiroferramentas.api.parceiro_api.unit.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.CarrinhoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.CompradorRepository;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.CarrinhoService;

@ExtendWith(MockitoExtension.class)
public class CarrinhoServiceTest {

    @Mock private CarrinhoRepository repository;
    @Mock private CompradorRepository compradorRepo;
    @Mock private UsuarioRepository usuarioRepo;
    @Mock private FerramentaRepository ferramentaRepo;

    @InjectMocks private CarrinhoService service;

    static Usuario usuario;
    static Comprador comprador;
    static List<Ferramenta> ferramentas;
    static List<ItemCarrinho> carrinho;

    @BeforeAll
    public static void setup() {
        usuario = CreateMockedData.getInstance().getUsuarios().get(3);
        comprador = CreateMockedData.getInstance().getCompradores().get(3);
        ferramentas = CreateMockedData.getInstance().getFerramentas();
    }

    @Test
    @DisplayName("Deve recuperar carrinho do usuário")
    void recuperarCarrinhoTeste() {
        comprador.setId(UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f"));
        int tamanhoCarrinho = 3;
        carrinho = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas);

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(repository.findItemCarrinhoByUsuarioId(comprador.getId())).thenReturn(carrinho);

        List<ItemCarrinho> response = service.recuperarCarrinho(usuario.getUsername());

        verify(repository, times(1)).findItemCarrinhoByUsuarioId(comprador.getId());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(comprador, response.get(0).getComprador());
        Assertions.assertEquals(tamanhoCarrinho, response.size());

    }

    @Test
    @DisplayName("Deve salvar um item no carrinho")
    void salvarItemTeste() {
        comprador.setId(UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f"));
        int tamanhoCarrinho = 1;
        int quantidadeDoItem = 2;
        
        ItemCarrinho item = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas).getFirst();
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
        Assertions.assertEquals(comprador, response.getComprador());
        Assertions.assertEquals(quantidadeDoItem, response.getQuantidade());
        Assertions.assertEquals(item.getFerramenta().getNome(), response.getFerramenta().getNome());
    }

    @Test
    @DisplayName("Deve salvar vários itens no carrinho")
    void salvarTodosTeste() {
        comprador.setId(UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f"));
        int tamanhoCarrinho = 3;
        carrinho = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas);
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
        Assertions.assertEquals(comprador, response.get(0).getComprador());
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
        comprador.setId(UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f"));
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        
        ItemCarrinho item = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas).getFirst();
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
        comprador.setId(UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f"));
        comprador.setCarrinhoItens(new ArrayList<>(CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas)));
        carrinho = CreateMockedData.getInstance().getCarrinho(tamanhoCarrinho, false, comprador, ferramentas);

        Mockito.when(usuarioRepo.findUsuarioByUsername(usuario.getUsername())).thenReturn(usuario);
        Mockito.when(repository.findItemCarrinhoByUsuarioId(usuario.getId())).thenReturn(carrinho);

        service.removerTodos(usuario.getUsername());

        verify(repository, times(1)).deleteAll(carrinho);
    }

}
