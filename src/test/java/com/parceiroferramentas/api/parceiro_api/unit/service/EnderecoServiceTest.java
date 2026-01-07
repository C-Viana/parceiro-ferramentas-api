package com.parceiroferramentas.api.parceiro_api.unit.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import com.parceiroferramentas.api.parceiro_api.enums.ESTADOS;
import com.parceiroferramentas.api.parceiro_api.enums.UF;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.repository.EnderecoRepository;
import com.parceiroferramentas.api.parceiro_api.repository.UsuarioRepository;
import com.parceiroferramentas.api.parceiro_api.service.EnderecoService;

@ExtendWith(MockitoExtension.class)
public class EnderecoServiceTest {

    @Mock
    private EnderecoRepository repository;

    @Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private EnderecoService service;

    private static List<Endereco> mockedEnderecos;
    private static List<Usuario> mockedUsuarios;

    @BeforeAll
    static void setup() {
        mockedUsuarios = CreateMockedData.getInstance().getUsuarios();
        mockedEnderecos = CreateMockedData.getInstance().getEnderecos(mockedUsuarios);
    }

    @Test
    @DisplayName("Deve cadastrar um novo endereço")
    void cadastrarNovoEndereco() {
        Integer usuarioId = 3;
        String logradouro = "Rua Seis";
        Integer numero = 10;
        String bairro = "Boa Esperança";
        String cidade = "Timon";
        ESTADOS estado = ESTADOS.MARANHAO;
        UF uf = UF.MA;
        String cep = "65636830";
        String referencia = null;
        Boolean principal = true;
        Usuario owner = mockedUsuarios.get(usuarioId);
        owner.setId(usuarioId.longValue());

        Endereco request = new Endereco(null, logradouro, numero, bairro, cidade, estado, uf, cep, referencia, principal, mockedUsuarios.get(3));
        Endereco response = new Endereco(5L, logradouro, numero, bairro, cidade, estado, uf, cep, referencia, principal, mockedUsuarios.get(3));

        when(usuarioRepo.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(repository.save(request)).thenReturn(response);
        Endereco resultado = service.create(usuarioId.longValue(), request);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getLogradouro()).contains(logradouro);
        Assertions.assertThat(resultado.getUsuario()).isEqualTo(resultado.getUsuario());
        verify(repository, times(1)).save(request);
    }

    @Test
    @DisplayName("Deve atualizar um endereço cadastrado")
    void atualizarEnderecoCadastrado() {
        Integer usuarioId = 4;
        String logradouro = "Travessa Oeste";
        Integer numero = 20;
        String bairro = "Jardim Aero Rancho";
        String cidade = "Campo Grande";
        ESTADOS estado = ESTADOS.MATO_GROSSO_DO_SUL;
        UF uf = UF.MS;
        String cep = "79083411";
        String referencia = null;
        Boolean principal = true;
        Usuario owner = mockedUsuarios.get(usuarioId);
        owner.setId(usuarioId.longValue());

        Endereco request = new Endereco(null, logradouro, numero, bairro, cidade, estado, uf, cep, referencia, principal, mockedUsuarios.get(usuarioId));
        Endereco response = new Endereco(6L, logradouro, numero, bairro, cidade, estado, uf, cep, referencia, principal, mockedUsuarios.get(usuarioId));

        when(usuarioRepo.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(repository.save(request)).thenReturn(response);
        Endereco resultado = service.create(usuarioId.longValue(), request);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getLogradouro()).isEqualTo(response.getLogradouro());
        Assertions.assertThat(resultado.getUsuario()).isEqualTo(response.getUsuario());

        String novaReferencia = "Em frente a padaria Lopes";
        Endereco reqAtualizacao = new Endereco(6L, logradouro, numero, bairro, cidade, estado, uf, cep, novaReferencia, principal, mockedUsuarios.get(usuarioId));
        
        when(repository.findById(resultado.getId())).thenReturn(Optional.of(reqAtualizacao));
        when(repository.save(reqAtualizacao)).thenReturn(reqAtualizacao);
        resultado = service.update(resultado.getId(), reqAtualizacao);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getLogradouro()).isEqualTo(reqAtualizacao.getLogradouro());
        Assertions.assertThat(resultado.getUsuario()).isEqualTo(reqAtualizacao.getUsuario());
        Assertions.assertThat(resultado.getReferencia()).isEqualTo(reqAtualizacao.getReferencia());
        verify(repository, times(1)).save(request);
    }

    @Test
    @DisplayName("Deve remover um endereço cadastrado")
    void removerEnderecoCadastrado() {
        Integer usuarioId = 2;
        String logradouro = "Rua Monsenhor Antero";
        Integer numero = 30;
        String bairro = "Cidade dos Funcionários";
        String cidade = "Fortaleza";
        ESTADOS estado = ESTADOS.CEARA;
        UF uf = UF.CE;
        String cep = "60822475";
        String referencia = null;
        Boolean principal = true;
        Usuario owner = mockedUsuarios.get(usuarioId);
        owner.setId(usuarioId.longValue());

        Endereco endereco = new Endereco(7L, logradouro, numero, bairro, cidade, estado, uf, cep, referencia, principal, mockedUsuarios.get(3));

        when(repository.findById(endereco.getId())).thenReturn(Optional.ofNullable(endereco));

        service.delete(endereco.getId());
        verify(repository, times(1)).findById(endereco.getId());
        verify(repository, times(1)).delete(endereco);
    }

    @Test
    @DisplayName("Deve retornar página de endereços")
    void buscarEnderecosRetornaSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Endereco> paginaMock = new PageImpl<>(mockedEnderecos, pageable, 1);

        when(repository.findAll(pageable)).thenReturn(paginaMock);

        Page<Endereco> resultado = service.findAll(pageable);

        Assertions.assertThat(resultado).hasSizeGreaterThan(0);
        Assertions.assertThat(resultado.getContent().get(0).getLogradouro()).contains(mockedEnderecos.get(0).getLogradouro());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar o endereço pesquisado por ID")
    void buscarEnderecoPorIdComSucesso() {
        Endereco request = mockedEnderecos.get(1);
        request.setId(2L);

        when(repository.findById(request.getId())).thenReturn(Optional.of(request));

        Endereco resultado = service.findById(request.getId());

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getLogradouro()).isEqualTo(request.getLogradouro());
        verify(repository, times(1)).findById(request.getId());
    }

    @Test
    @DisplayName("Deve retornar apenas os endereços do usuario")
    void buscarEnderecoDeUmUsuario() {
        Integer usuarioId = 0;
        String logradouro = "Rua Valter Tavares dos Santos";
        Integer numero = 40;
        String bairro = "Distrito Industrial";
        String cidade = "Itapeva";
        ESTADOS estado = ESTADOS.SAO_PAULO;
        UF uf = UF.SP;
        String cep = "18410635";
        String referencia = null;
        Boolean principal = true;
        Usuario owner = mockedUsuarios.get(usuarioId);
        owner.setId(usuarioId.longValue());

        Endereco endereco1 = mockedEnderecos.get(0);
        endereco1.setId(1L);
        Endereco endereco2 = new Endereco(8L, logradouro, numero, bairro, cidade, estado, uf, cep, referencia, principal, mockedUsuarios.get(usuarioId));

        when(usuarioRepo.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(repository.findEnderecoByUsuarioId(owner.getId())).thenReturn(List.of(endereco1, endereco2));

        List<Endereco> resultados = service.findUsuarioEnderecos(owner.getId());

        Assertions.assertThat(resultados).hasSize(2);
        Assertions.assertThat(resultados.get(0).getLogradouro()).isEqualTo(endereco1.getLogradouro());
        Assertions.assertThat(resultados.get(1).getLogradouro()).isEqualTo(endereco2.getLogradouro());
        verify(repository, times(1)).findEnderecoByUsuarioId(owner.getId());
    }

}
