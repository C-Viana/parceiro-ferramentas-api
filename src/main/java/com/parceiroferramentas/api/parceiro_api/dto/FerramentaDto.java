package com.parceiroferramentas.api.parceiro_api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FerramentaDto {

    private Long id;
    private String nome;
    private String modelo;
    private String fabricante;
    private String tipo;
    private String descricao;
    private Map<String, Object> caracteristicas;
    private List<String> itens_inclusos;
    private boolean disponibilidade;
    private Double preco_aluguel;
    private Double preco_venda;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate criado_em;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate atualizado_em;

    public FerramentaDto(){}

    public FerramentaDto(Long id, String nome, String modelo, String fabricante, String tipo, String descricao,
            Map<String, Object> caracteristicas, List<String> itens_inclusos, boolean disponibilidade, Double preco_aluguel,
            Double preco_venda, LocalDate criado_em, LocalDate atualizado_em) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.tipo = tipo;
        this.descricao = descricao;
        this.caracteristicas = caracteristicas;
        this.itens_inclusos = itens_inclusos;
        this.disponibilidade = disponibilidade;
        this.preco_aluguel = preco_aluguel;
        this.preco_venda = preco_venda;
        this.criado_em = criado_em;
        this.atualizado_em = atualizado_em;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Map<String, Object> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(Map<String, Object> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public List<String> getItens_inclusos() {
        return itens_inclusos;
    }

    public void setItens_inclusos(List<String> itens_inclusos) {
        this.itens_inclusos = itens_inclusos;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public Double getPreco_aluguel() {
        return preco_aluguel;
    }

    public void setPreco_aluguel(Double preco_aluguel) {
        this.preco_aluguel = preco_aluguel;
    }

    public Double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(Double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public LocalDate getCriado_em() {
        return criado_em;
    }

    public void setCriado_em(LocalDate criado_em) {
        this.criado_em = criado_em;
    }

    public LocalDate getAtualizado_em() {
        return atualizado_em;
    }

    public void setAtualizado_em(LocalDate atualizado_em) {
        this.atualizado_em = atualizado_em;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
        result = prime * result + ((fabricante == null) ? 0 : fabricante.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
        result = prime * result + ((caracteristicas == null) ? 0 : caracteristicas.hashCode());
        result = prime * result + ((itens_inclusos == null) ? 0 : itens_inclusos.hashCode());
        result = prime * result + (disponibilidade ? 1231 : 1237);
        result = prime * result + ((preco_aluguel == null) ? 0 : preco_aluguel.hashCode());
        result = prime * result + ((preco_venda == null) ? 0 : preco_venda.hashCode());
        result = prime * result + ((criado_em == null) ? 0 : criado_em.hashCode());
        result = prime * result + ((atualizado_em == null) ? 0 : atualizado_em.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FerramentaDto other = (FerramentaDto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (modelo == null) {
            if (other.modelo != null)
                return false;
        } else if (!modelo.equals(other.modelo))
            return false;
        if (fabricante == null) {
            if (other.fabricante != null)
                return false;
        } else if (!fabricante.equals(other.fabricante))
            return false;
        if (tipo == null) {
            if (other.tipo != null)
                return false;
        } else if (!tipo.equals(other.tipo))
            return false;
        if (descricao == null) {
            if (other.descricao != null)
                return false;
        } else if (!descricao.equals(other.descricao))
            return false;
        if (caracteristicas == null) {
            if (other.caracteristicas != null)
                return false;
        } else if (!caracteristicas.equals(other.caracteristicas))
            return false;
        if (itens_inclusos == null) {
            if (other.itens_inclusos != null)
                return false;
        } else if (!itens_inclusos.equals(other.itens_inclusos))
            return false;
        if (disponibilidade != other.disponibilidade)
            return false;
        if (preco_aluguel == null) {
            if (other.preco_aluguel != null)
                return false;
        } else if (!preco_aluguel.equals(other.preco_aluguel))
            return false;
        if (preco_venda == null) {
            if (other.preco_venda != null)
                return false;
        } else if (!preco_venda.equals(other.preco_venda))
            return false;
        if (criado_em == null) {
            if (other.criado_em != null)
                return false;
        } else if (!criado_em.equals(other.criado_em))
            return false;
        if (atualizado_em == null) {
            if (other.atualizado_em != null)
                return false;
        } else if (!atualizado_em.equals(other.atualizado_em))
            return false;
        return true;
    }

    
}
