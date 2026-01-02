package com.parceiroferramentas.api.parceiro_api.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.exception.InternalApplicationException;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.repository.FerramentaRepository;
import com.parceiroferramentas.api.parceiro_api.service.FerramentaService;

@ExtendWith(MockitoExtension.class)
public class FerramentaServiceTest {

    private final String BAD_REQUEST_MESSAGE = "Campos obrigatórios não podem ser nulos, vazios ou com preços menores ou iguais a zero";

    @Mock
    private FerramentaRepository repository;

    @InjectMocks
    private FerramentaService service;

    private static List<Ferramenta> mockedFerramentas;

    @BeforeAll
    static void setup() {
        mockedFerramentas = CreateMockedData.getInstance().getFerramentas();
    }

    @Test
    @DisplayName("Deve retornar página de ferramentas quando buscar por tipo")
    void buscarPorTipo_comTipoValido_retornaPagina() {
        // GIVEN - dados de entrada
        String tipoBuscado = "motosserra";
        Pageable pageable = PageRequest.of(0, 10);
        List<Ferramenta> filteredResult = mockedFerramentas.stream().filter(item -> {
            return item.getTipo().equalsIgnoreCase(tipoBuscado);
        }).toList();
        Page<Ferramenta> paginaMock = new PageImpl<>(filteredResult, pageable, 1);

        // WHEN - simula o repository
        when(repository.findByTipoEqualsIgnoreCase(tipoBuscado, pageable)).thenReturn(paginaMock);

        // THEN - executa e verifica
        Page<Ferramenta> resultado = service.findAllByType(tipoBuscado, pageable);

        Assertions.assertThat(resultado).hasSizeGreaterThan(0);
        Assertions.assertThat(resultado.getContent().get(0).getNome()).contains("Motosserra");

        verify(repository, times(1)).findByTipoEqualsIgnoreCase(tipoBuscado, pageable);
    }

    @Test
    @DisplayName("Deve retornar todas as ferramentas quando tipo for vazio")
    void buscarPorTipo_comTipoVazio_retornaTodas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ferramenta> paginaTodas = new PageImpl<>(mockedFerramentas, pageable, mockedFerramentas.size());

        when(repository.findAll(pageable)).thenReturn(paginaTodas);

        Page<Ferramenta> resultado = service.findAllByType("", pageable);

        Assertions.assertThat(resultado).hasSize(mockedFerramentas.size());
        verify(repository, never()).findByTipoEqualsIgnoreCase(any(), any());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar todas as ferramentas quando tipo for nulo")
    void buscarPorTipo_comTipoNulo_retornaTodas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ferramenta> paginaTodas = new PageImpl<>(mockedFerramentas, pageable, mockedFerramentas.size());

        when(repository.findAll(pageable)).thenReturn(paginaTodas);

        Page<Ferramenta> resultado = service.findAllByType(null, pageable);

        Assertions.assertThat(resultado).hasSize(mockedFerramentas.size());
        verify(repository, never()).findByTipoEqualsIgnoreCase(any(), any());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar todas as ferramentas quando tipo for apenas espaços")
    void buscarPorTipo_comTipoApenasEspacos_retornaTodas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ferramenta> paginaTodas = new PageImpl<>(mockedFerramentas, pageable, mockedFerramentas.size());

        when(repository.findAll(pageable)).thenReturn(paginaTodas);

        Page<Ferramenta> resultado = service.findAllByType("     ", pageable);

        Assertions.assertThat(resultado).hasSize(mockedFerramentas.size());
        verify(repository, never()).findByTipoEqualsIgnoreCase(any(), any());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar página de ferramentas quando buscar por tipo em maiúsculo")
    void buscarPorTipo_comTipoValidoEMaiusculo_retornaPagina() {
        String tipoBuscado = "MOTOSSERRA";
        Pageable pageable = PageRequest.of(0, 10);
        List<Ferramenta> filteredResult = mockedFerramentas.stream().filter(item -> {
            return item.getTipo().equalsIgnoreCase(tipoBuscado);
        }).toList();
        Page<Ferramenta> paginaMock = new PageImpl<>(filteredResult, pageable, 1);

        when(repository.findByTipoEqualsIgnoreCase(tipoBuscado, pageable)).thenReturn(paginaMock);

        Page<Ferramenta> resultado = service.findAllByType(tipoBuscado, pageable);

        Assertions.assertThat(resultado).hasSizeGreaterThan(0);
        Assertions.assertThat(resultado.getContent().get(0).getNome()).contains("Motosserra");

        verify(repository, times(1)).findByTipoEqualsIgnoreCase(tipoBuscado, pageable);
    }

    @Test
    @DisplayName("Deve retornar ferramenta quando buscar por ID válido")
    void buscarPorId_comIdValido_retornaFerramenta() {
        Long idBuscado = 1L;
        Ferramenta ferramentaBuscada = mockedFerramentas.stream().filter(item -> {return item.getId()==idBuscado;}).findFirst().orElse(null);

        when(repository.findById(idBuscado)).thenReturn(Optional.ofNullable(ferramentaBuscada));

        Ferramenta resultado = service.findById(idBuscado);

        Assertions.assertThat(resultado).isNotNull();
        Assertions. assertThat(resultado).isEqualTo(ferramentaBuscada);
        Assertions.assertThat(resultado.getId()).isEqualTo(idBuscado);
        verify(repository, times(1)).findById(idBuscado);
    }

    @Test
    @DisplayName("Deve retornar nulo quando buscar por ID inexistente")
    void buscarPorId_comIdInvalido_retornaNulo() {
        Long idBuscado = 9999L;

        when(repository.findById(idBuscado)).thenReturn(Optional.empty());

        Ferramenta resultado = service.findById(idBuscado);

        Assertions.assertThat(resultado).isNull();
        verify(repository, times(1)).findById(idBuscado);
    }

    @Test
    @DisplayName("Deve criar uma nova ferramenta")
    void cadastrarNovaFerramenta_comCamposValidos() {
        Ferramenta novaFerramenta = CreateMockedData.getInstance().getNovaFerramenta();
        Ferramenta ferramentaCriada = novaFerramenta;
        ferramentaCriada.setId(1L);
        String nome = novaFerramenta.getNome();
        String modelo = novaFerramenta.getModelo();
        String tipo = novaFerramenta.getTipo();
        String fabricante = novaFerramenta.getFabricante();
        String descricao = novaFerramenta.getDescricao();
        Map<String, Object> caracteristicas = new HashMap<>();
        caracteristicas.put("Atributo A", "Caracteristica A");
        caracteristicas.put("Atributo B", "Caracteristica B");
        caracteristicas.put("Atributo C", "Caracteristica C");
        caracteristicas.put("Atributo D", "Caracteristica D");
        List<String> itens = Arrays.asList("Item A", "Item B", "Item C", "Item D", "Item E");
        boolean disponibilidade = true;
        Double preco_aluguel = 99.99;
        Double preco_venda = 999.99;
        LocalDate data_criacao = LocalDate.now();
        LocalDate data_atualizacao = LocalDate.now();

        when(repository.save(novaFerramenta)).thenReturn(ferramentaCriada);

        Ferramenta resultado = service.create(novaFerramenta);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getId()).isEqualTo(1);
        Assertions.assertThat(resultado.getNome()).isEqualTo(nome);
        Assertions.assertThat(resultado.getModelo()).isEqualTo(modelo);
        Assertions.assertThat(resultado.getFabricante()).isEqualTo(fabricante);
        Assertions.assertThat(resultado.getDescricao()).isEqualTo(descricao);
        Assertions.assertThat(resultado.getTipo()).isEqualTo(tipo);
        Assertions.assertThat(resultado.getCaracteristicas()).isEqualTo(caracteristicas);
        Assertions.assertThat(resultado.getItens_inclusos()).isEqualTo(itens);
        Assertions.assertThat(resultado.getDisponibilidade()).isEqualTo(disponibilidade);
        Assertions.assertThat(resultado.getPreco_aluguel()).isEqualTo(preco_aluguel);
        Assertions.assertThat(resultado.getPreco_venda()).isEqualTo(preco_venda);
        Assertions.assertThat(resultado.getCriado_em()).isEqualTo(data_criacao);
        Assertions.assertThat(resultado.getAtualizado_em()).isEqualTo(data_atualizacao);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro BAD REQUEST ao cadastrar uma nova ferramenta com campos vazios")
    void cadastrarNovaFerramenta_comCamposObrigatoriosVazios() {
        Ferramenta novaFerramenta = CreateMockedData.getInstance().getNovaFerramenta();
        novaFerramenta.setNome("");
        novaFerramenta.setModelo("");
        novaFerramenta.setFabricante("");
        novaFerramenta.setTipo("");
        novaFerramenta.setPreco_aluguel(0D);
        novaFerramenta.setPreco_venda(0D);

        Assertions
            .assertThatThrownBy(() -> service.create(novaFerramenta))
            .isInstanceOf(InternalApplicationException.class)
            .hasMessage(BAD_REQUEST_MESSAGE);
        
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro BAD REQUEST ao cadastrar uma nova ferramenta com campos nulos")
    void cadastrarNovaFerramenta_comCamposObrigatoriosNulos() {
        Ferramenta novaFerramenta = CreateMockedData.getInstance().getNovaFerramenta();
        novaFerramenta.setNome(null);
        novaFerramenta.setModelo(null);
        novaFerramenta.setFabricante(null);
        novaFerramenta.setTipo(null);
        novaFerramenta.setPreco_aluguel(null);
        novaFerramenta.setPreco_venda(null);

        Assertions
            .assertThatThrownBy(() -> service.create(novaFerramenta))
            .isInstanceOf(InternalApplicationException.class)
            .hasMessage(BAD_REQUEST_MESSAGE);
        
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro BAD REQUEST ao cadastrar uma nova ferramenta com preços menores que zero")
    void cadastrarNovaFerramenta_comPrecosMenoresQueZero() {
        Ferramenta novaFerramenta = CreateMockedData.getInstance().getNovaFerramenta();
        novaFerramenta.setPreco_aluguel(-5.50D);
        novaFerramenta.setPreco_venda(-20.00D);

        Assertions
            .assertThatThrownBy(() -> service.create(novaFerramenta))
            .isInstanceOf(InternalApplicationException.class)
            .hasMessage(BAD_REQUEST_MESSAGE);
        
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve atualizar o nome da ferramenta")
    void atualizarFerramenta_comNovosDadosValidos() {
        String nomeNovo = "NOME ATUALIZADO";
        LocalDate dataAtualizada = LocalDate.now();
        Ferramenta ferramentaAntes = mockedFerramentas.stream().filter(item -> item.getId()==2).findFirst().orElse(null);
        String nomeAnterior = ferramentaAntes.getNome();
        ferramentaAntes.setNome(nomeNovo);

        Ferramenta ferramentaAtualizada = ferramentaAntes;
        ferramentaAtualizada.setNome(nomeNovo);
        ferramentaAtualizada.setAtualizado_em(dataAtualizada);

        when(repository.save(ferramentaAntes)).thenReturn(ferramentaAtualizada);

        Ferramenta response = service.update(ferramentaAntes);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNome()).isNotEqualTo(nomeAnterior);
        Assertions.assertThat(response.getNome()).isEqualTo(nomeNovo);
        Assertions.assertThat(response.getAtualizado_em()).isEqualTo(dataAtualizada);
        Assertions.assertThat(response.getCriado_em()).isNotEqualTo(dataAtualizada);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro BAD REQUEST ao atualizar ferramenta com campos vazios")
    void atualizarFerramenta_comCamposVazios() {
        Ferramenta ferramentaAtualizada = CreateMockedData.getInstance().getNovaFerramenta();
        ferramentaAtualizada.setNome("");
        ferramentaAtualizada.setModelo("");
        ferramentaAtualizada.setFabricante("");
        ferramentaAtualizada.setTipo("");
        ferramentaAtualizada.setPreco_aluguel(0D);
        ferramentaAtualizada.setPreco_venda(0D);

        Assertions
            .assertThatThrownBy(() -> service.update(ferramentaAtualizada))
            .isInstanceOf(InternalApplicationException.class)
            .hasMessage(BAD_REQUEST_MESSAGE);
        
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro BAD REQUEST ao atualizar ferramenta com campos nulos")
    void atualizarFerramenta_comCamposNulos() {
        Ferramenta ferramentaAtualizada = CreateMockedData.getInstance().getNovaFerramenta();
        ferramentaAtualizada.setNome(null);
        ferramentaAtualizada.setModelo(null);
        ferramentaAtualizada.setFabricante(null);
        ferramentaAtualizada.setTipo(null);
        ferramentaAtualizada.setPreco_aluguel(null);
        ferramentaAtualizada.setPreco_venda(null);

        Assertions
            .assertThatThrownBy(() -> service.update(ferramentaAtualizada))
            .isInstanceOf(InternalApplicationException.class)
            .hasMessage(BAD_REQUEST_MESSAGE);
        
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro BAD REQUEST ao atualizar uma nova ferramenta com preços menores que zero")
    void atualizarFerramenta_comPrecosMenoresQueZero() {
        Ferramenta ferramentaAtualizada = CreateMockedData.getInstance().getNovaFerramenta();
        ferramentaAtualizada.setPreco_aluguel(-5.50D);
        ferramentaAtualizada.setPreco_venda(-20.00D);

        Assertions
            .assertThatThrownBy(() -> service.update(ferramentaAtualizada))
            .isInstanceOf(InternalApplicationException.class)
            .hasMessage(BAD_REQUEST_MESSAGE);
        
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve remover a ferramenta do banco e retornar vazio")
    void apagarFerramenta_comIdValido() {
        Ferramenta ferramentaNova = CreateMockedData.getInstance().getNovaFerramenta();

        when(repository.findById(ferramentaNova.getId())).thenReturn(Optional.ofNullable(ferramentaNova));

        service.delete(ferramentaNova.getId());
        verify(repository, times(1)).findById(ferramentaNova.getId());
        verify(repository, times(1)).deleteById(ferramentaNova.getId());
    }

    @Test
    @DisplayName("Não deve chamar o método de deleção ao tentar remover uma ferramenta inexistente")
    void apagarFerramenta_comIdInexistente() {
        Long idInexistente = 9999L;
        when(repository.findById(idInexistente)).thenReturn(null);

        service.delete(idInexistente);
        verify(repository, times(1)).findById(idInexistente);
        verify(repository, times(0)).deleteById(idInexistente);
    }

}
