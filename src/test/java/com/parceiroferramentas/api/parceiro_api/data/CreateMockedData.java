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
import java.util.UUID;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.parceiroferramentas.api.parceiro_api.dto.ItemCarrinhoRequestDto;
import com.parceiroferramentas.api.parceiro_api.enums.Estados;
import com.parceiroferramentas.api.parceiro_api.enums.PerfilAcesso;
import com.parceiroferramentas.api.parceiro_api.enums.UF;
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;
import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.model.pedido.ItemPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.StatusPedido;
import com.parceiroferramentas.api.parceiro_api.model.pedido.TipoPedido;

public class CreateMockedData {

    private File jsonFerramentas = new File("src/test/resources/FerramentasMock.json");
    private File jsonItemCarrinhoRequestDto = new File("src/test/resources/ItemCarrinhoRequestDto.json");
    private final String senhaEncriptada = "$2a$10$Ur9TKcWfC2oVKBF9jOnaiOxl/FhGjHIdzmG158wxvzrGk9UDqxJsS";
    private final List<UUID> IDS = List.of(
        UUID.fromString("33476ff5-27cf-4113-ab83-7486e9d7e2dd"),
        UUID.fromString("3f3b406b-b6b2-4a13-afe6-35746410cd86"),
        UUID.fromString("cccdefd5-bbae-4b46-9ac5-425ea28fbf5f"),
        UUID.fromString("76ea733f-617a-4a17-b425-bba6cfd5a21f"),
        UUID.fromString("0f0b3ab2-cacb-47d9-a00c-68e2340da9c9")
    );

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

        mockedUsuarios.add(new Usuario(
            (IDS.get(0)),
            "useradmin",
            senhaEncriptada,
            true,
            true,
            true,
            true,
            Arrays.asList(new Permissao(1L, PerfilAcesso.ADMIN))
        ));
        mockedUsuarios.add(new Usuario(
            (IDS.get(1)),
            "usergerente",
            senhaEncriptada,
            true,
            true,
            true,
            true,
            Arrays.asList(new Permissao(2L, PerfilAcesso.GERENTE))
        ));
        mockedUsuarios.add(new Usuario(
            (IDS.get(2)),
            "uservendedor",
            senhaEncriptada,
            true,
            true,
            true,
            true,
            Arrays.asList(new Permissao(3L, PerfilAcesso.VENDEDOR))
        ));
        mockedUsuarios.add(new Usuario(
            (IDS.get(3)),
            "usercliente",
            senhaEncriptada,
            true,
            true,
            true,
            true,
            Arrays.asList(new Permissao(4L, PerfilAcesso.CLIENTE))
        ));
        mockedUsuarios.add(new Usuario(
            (IDS.get(4)),
            "usuarioinexistente",
            senhaEncriptada,
            true,
            true,
            true,
            true,
            Arrays.asList(new Permissao(4L, PerfilAcesso.CLIENTE))
        ));

        return mockedUsuarios;
    }

    public List<Comprador> getCompradores() {
        List<Comprador> mockedUsuarios = new ArrayList<>();

        mockedUsuarios.add(new Comprador(
            (IDS.get(0)),
            "40681843802",
            "João Oliveira da Silva",
            LocalDate.of(1992, 4, 15),
            "josilva@parceiro.com.br",
            "11930306080",
            List.of(),
            List.of()
        ));
        mockedUsuarios.add(new Comprador(
            (IDS.get(1)),
            "49055010678",
            "Cláudia Ferreira dos Santos",
            LocalDate.of(1985, 7, 22),
            "cfsantos@parceiro.com.br",
            "13978456421",
            List.of(),
            List.of()
        ));
        mockedUsuarios.add(new Comprador(
            (IDS.get(2)),
            "70052289310",
            "Luana Correia Costa",
            LocalDate.of(1999, 10, 9),
            "lccosta@parceiro.com.br",
            "13946742103",
            List.of(),
            List.of()
        ));
        mockedUsuarios.add(new Comprador(
            (IDS.get(3)),
            "19278755003",
            "Marcos Castro de Almeida",
            LocalDate.of(1978, 3, 13),
            "mcalmeida@gmail.com",
            "11985253710",
            List.of(),
            List.of()
        ));
        mockedUsuarios.add(new Comprador(
            (IDS.get(4)),
            "22641593001",
            "Usuario Não Cadastrado",
            LocalDate.of(2002, 11, 28),
            "desconhecido@outlook.com",
            "21945608812",
            List.of(),
            List.of()
        ));

        return mockedUsuarios;
    }

    public List<Permissao> getPermissoes() {
        List<Permissao> permissoes = new ArrayList<>();
        permissoes.add(new Permissao(null, PerfilAcesso.ADMIN));
        permissoes.add(new Permissao(null, PerfilAcesso.GERENTE));
        permissoes.add(new Permissao(null, PerfilAcesso.VENDEDOR));
        permissoes.add(new Permissao(null, PerfilAcesso.CLIENTE));

        return permissoes;
    }

    public List<Endereco> getEnderecos(List<Comprador> mockedCompradores) {
        List<Endereco> mockedEnderecos = new ArrayList<>();

        mockedEnderecos.add(new Endereco(null, "Rua José Eugênio da Silva", 122, "Parque Santa Teresa", "Carapicuíba", Estados.SAO_PAULO, UF.SP, "06340400", null, true, mockedCompradores.get(0)));
        mockedEnderecos.add(new Endereco(null, "Rua Aline", 74, "Parque dos Camargos", "Barueri", Estados.SAO_PAULO, UF.SP, "06436110", "Portão amarelo após a adega do Jurandir", true, mockedCompradores.get(1)));
        mockedEnderecos.add(new Endereco(null, "Rua Serra Leoa", 328, "Rochdale", "Osasco", Estados.SAO_PAULO, UF.SP, "06220059", "Próximo da praça", true, mockedCompradores.get(2)));
        mockedEnderecos.add(new Endereco(null, "Rua Florindo Redivo", 14, "Vila Esperança", "Maringá", Estados.PARANA, UF.PR, "87020520", null, true, mockedCompradores.get(3)));

        return mockedEnderecos;
    }

    public List<ItemCarrinho> getCarrinho( int quantidadeItens, boolean comId, Comprador donoCarrinho, List<Ferramenta> ferramentas ) {
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
            item.setComprador(donoCarrinho);
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

    public Pedido getPedido(TipoPedido tipoPedido, int prazo, Comprador usuario, Endereco endereco, List<ItemPedido> itens) {
        Pedido pedidoModel = new Pedido();
        pedidoModel.setComprador(usuario);
        pedidoModel.setEndereco(endereco);
        pedidoModel.setTipo(TipoPedido.COMPRA);
        pedidoModel.setSituacao(StatusPedido.PENDENTE);
        pedidoModel.setDataCriacao(Instant.now());
        pedidoModel.setDataAtualizacao(Instant.now());
        pedidoModel.setItens(itens);

        if(tipoPedido == TipoPedido.COMPRA)
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
