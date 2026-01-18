package com.parceiroferramentas.api.parceiro_api.data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoRequestDto;
import com.parceiroferramentas.api.parceiro_api.enums.ESTADOS;
import com.parceiroferramentas.api.parceiro_api.enums.PERFIL_ACESSO;
import com.parceiroferramentas.api.parceiro_api.enums.UF;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.model.pedido.ItemPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.STATUS_PEDIDO;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TIPO_PEDIDO;

public class CreateMockedData {

    private File jsonFerramentas = new File("src/test/resources/FerramentasMock.json");
    private File jsonItemCarrinhoRequestDto = new File("src/test/resources/ItemCarrinhoRequestDto.json");

    public static CreateMockedData getInstance() {
        return new CreateMockedData();
    }

    public List<Ferramenta> getFerramentas() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(module);

        try {
            return Arrays.asList(mapper.readValue(jsonFerramentas, Ferramenta[].class));
        } catch (StreamReadException e) {
            throw new RuntimeException("Erro na leitura do arquivo Json", e);
        } catch (DatabindException e) {
            throw new RuntimeException("Erro ao mapear o Json para o objeto Ferramenta", e);
        } catch (IOException e) {
            throw new RuntimeException("Erro de I/O ao processar o arquivo Json", e);
        }
    }

    public Ferramenta getNovaFerramenta() {
        Ferramenta ferramentaCriada = new Ferramenta();
        String nome = "Ferramenta de teste " + System.currentTimeMillis();
        String modelo = "Unitários";
        String tipo = "Tipo de teste";
        String fabricante = "Dev e Qualidade";
        String descricao = "Ferramenta fictícia para validação em teste unitário";
        Map<String, Object> caracteristicas = new HashMap<>();
        caracteristicas.put("Atributo A", "Caracteristica A");
        caracteristicas.put("Atributo B", "Caracteristica B");
        caracteristicas.put("Atributo C", "Caracteristica C");
        caracteristicas.put("Atributo D", "Caracteristica D");
        List<String> itens = Arrays.asList("Item A", "Item B", "Item C", "Item D", "Item E");
        List<String> lista_imagens = Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg");
        boolean disponibilidade = true;
        Double preco_aluguel = 99.99;
        Double preco_venda = 999.99;
        LocalDate data_criacao = LocalDate.now();
        LocalDate data_atualizacao = data_criacao;

        //ferramentaCriada.setId(1L);
        ferramentaCriada.setNome(nome);
        ferramentaCriada.setModelo(modelo);
        ferramentaCriada.setTipo(tipo);
        ferramentaCriada.setFabricante(fabricante);
        ferramentaCriada.setDescricao(descricao);
        ferramentaCriada.setCaracteristicas(caracteristicas);
        ferramentaCriada.setItens_inclusos(itens);
        ferramentaCriada.setLista_imagens(lista_imagens);
        ferramentaCriada.setDisponibilidade(disponibilidade);
        ferramentaCriada.setPreco_aluguel(preco_aluguel);
        ferramentaCriada.setPreco_venda(preco_venda);
        ferramentaCriada.setCriado_em(data_criacao);
        ferramentaCriada.setAtualizado_em(data_atualizacao);

        return ferramentaCriada;
    }

    public List<Usuario> getUsuarios() {
        List<Usuario> mockedUsuarios = new ArrayList<>();
        String senhaEncriptada = "$2a$10$AzCbojDDN5urYBKGcQk2Oewne3YiHMOOGsyndFejY2rpjldua2KzK";

        mockedUsuarios.add(new Usuario(
            null, "useradmin", "João Oliveira da Silva", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(1L, PERFIL_ACESSO.ADMIN)), List.of(), List.of()
        ));
        mockedUsuarios.add(new Usuario(
            null, "usergerente", "Cláudia Ferreira dos Santos", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(2L, PERFIL_ACESSO.GERENTE)), List.of(), List.of()
        ));
        mockedUsuarios.add(new Usuario(
            null, "uservendedor", "Luana Correia Costa", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(3L, PERFIL_ACESSO.VENDEDOR)), List.of(), List.of()
        ));
        mockedUsuarios.add(new Usuario(
            null, "usercliente", "Marcos Castro de Almeida", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(4L, PERFIL_ACESSO.CLIENTE)), List.of(), List.of()
        ));
        mockedUsuarios.add(new Usuario(
            null, "usuarioinexistente", "Usuario Não Cadastrado", senhaEncriptada, true, true, true, true, Arrays.asList(new Permissao(4L, PERFIL_ACESSO.CLIENTE)), List.of(), List.of()
        ));

        return mockedUsuarios;
    }

    public List<Permissao> getPermissoes() {
        List<Permissao> permissoes = new ArrayList<>();
        permissoes.add(new Permissao(null, PERFIL_ACESSO.ADMIN));
        permissoes.add(new Permissao(null, PERFIL_ACESSO.GERENTE));
        permissoes.add(new Permissao(null, PERFIL_ACESSO.VENDEDOR));
        permissoes.add(new Permissao(null, PERFIL_ACESSO.CLIENTE));

        return permissoes;
    }

    public List<Endereco> getEnderecos(List<Usuario> mockedUsuarios) {
        List<Endereco> mockedEnderecos = new ArrayList<>();

        mockedEnderecos.add(new Endereco(null, "Rua José Eugênio da Silva", 122, "Parque Santa Teresa", "Carapicuíba", ESTADOS.SAO_PAULO, UF.SP, "06340400", null, true, mockedUsuarios.get(0)));
        mockedEnderecos.add(new Endereco(null, "Rua Aline", 74, "Parque dos Camargos", "Barueri", ESTADOS.SAO_PAULO, UF.SP, "06436110", "Portão amarelo após a adega do Jurandir", true, mockedUsuarios.get(1)));
        mockedEnderecos.add(new Endereco(null, "Rua Serra Leoa", 328, "Rochdale", "Osasco", ESTADOS.SAO_PAULO, UF.SP, "06220059", "Próximo da praça", true, mockedUsuarios.get(2)));
        mockedEnderecos.add(new Endereco(null, "Rua Florindo Redivo", 14, "Vila Esperança", "Maringá", ESTADOS.PARANA, UF.PR, "87020520", null, true, mockedUsuarios.get(3)));

        return mockedEnderecos;
    }

    public List<ItemCarrinho> getCarrinho( int quantidadeItens, boolean comId, Usuario donoCarrinho, List<Ferramenta> ferramentas ) {
        List<ItemCarrinho> carrinho = new ArrayList<>();

        for (int i = 0; i < quantidadeItens; i++) {
            ItemCarrinho item = new ItemCarrinho();
            Ferramenta ferramenta = ferramentas.get(i);
            item.setFerramenta(ferramenta);
            item.setPrecoAluguelMomento( BigDecimal.valueOf(ferramenta.getPreco_aluguel()) );
            item.setPrecoVendaMomento( BigDecimal.valueOf(ferramenta.getPreco_venda()) );
            item.setDataAdicao(Instant.now());
            item.setQuantidade((i+1));
            item.setUrlImage(ferramenta.getLista_imagens().get(0));
            item.setUsuario(donoCarrinho);
            if(comId) item.setId(Integer.toUnsignedLong((i+1)));
            carrinho.add(item);
        }

        return carrinho;
    }

    public List<ItemCarrinhoRequestDto> getItemCarrinhoRequest() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        try {
            return Arrays.asList(mapper.readValue(jsonItemCarrinhoRequestDto, ItemCarrinhoRequestDto[].class));
        } catch (StreamReadException e) {
            throw new RuntimeException("Erro na leitura do arquivo Json", e);
        } catch (DatabindException e) {
            throw new RuntimeException("Erro ao mapear o Json para o objeto Ferramenta", e);
        } catch (IOException e) {
            throw new RuntimeException("Erro de I/O ao processar o arquivo Json", e);
        }
    }

    public List<ItemPedido> getItensDoPedidoCompra(List<ItemCarrinho> carrinho) {
        List<ItemPedido> pedido = new ArrayList<>();
        for (int i = 0; i < carrinho.size(); i++) {
            ItemPedido item = new ItemPedido();
            item.setFerramenta(carrinho.get(i).getFerramenta());
            item.setPrecoUnitario(carrinho.get(i).getPrecoVendaMomento());
            item.setQuantidade(carrinho.get(i).getQuantidade());
            pedido.add(item);
        }
        return pedido;
    }

    public List<ItemPedido> getItensDoPedidoAluguel(List<ItemCarrinho> carrinho) {
        List<ItemPedido> pedido = new ArrayList<>();
        for (int i = 0; i < carrinho.size(); i++) {
            ItemPedido item = new ItemPedido();
            item.setFerramenta(carrinho.get(i).getFerramenta());
            item.setPrecoUnitario(carrinho.get(i).getPrecoAluguelMomento());
            item.setQuantidade(carrinho.get(i).getQuantidade());
            pedido.add(item);
        }
        return pedido;
    }

    public Pedido getPedido(TIPO_PEDIDO tipoPedido, int prazo, Usuario usuario, Endereco endereco, List<ItemPedido> itens) {
        Pedido pedidoModel = new Pedido();
        pedidoModel.setUsuario(usuario);
        pedidoModel.setEndereco(endereco);
        pedidoModel.setTipo(TIPO_PEDIDO.COMPRA);
        pedidoModel.setSituacao(STATUS_PEDIDO.PENDENTE);
        pedidoModel.setDataCriacao(Instant.now());
        pedidoModel.setDataAtualizacao(Instant.now());
        pedidoModel.setItens(itens);

        if(tipoPedido == TIPO_PEDIDO.COMPRA)
        pedidoModel.setValorTotal(
            itens.stream()
            .map( item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        else{
            BigDecimal somaTotal = itens.stream()
                .map( item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            pedidoModel.setValorTotal(
                somaTotal.multiply(BigDecimal.valueOf(prazo))
            );
        }
        return pedidoModel;
    }

}
