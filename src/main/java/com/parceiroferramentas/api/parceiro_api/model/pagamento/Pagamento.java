package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    private BigDecimal valor;

    @Column(name = "forma_pagamento", columnDefinition = "forma_pagamento")
    private TIPO_PAGAMENTO formaPagamento;
    
    private STATUS_PAGAMENTO situacao;

    @Column(name = "data_criacao")
    private Instant dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private Instant dataAtualizacao;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "codigo_transacao")
    private UUID codigoTransacao;

    @JdbcTypeCode(SqlTypes.JSON)
    private String detalhes;

    public Pagamento(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TIPO_PAGAMENTO getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(TIPO_PAGAMENTO formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public STATUS_PAGAMENTO getSituacao() {
        return situacao;
    }

    public void setSituacao(STATUS_PAGAMENTO situacao) {
        this.situacao = situacao;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Instant getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Instant dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public UUID getCodigoTransacao() {
        return codigoTransacao;
    }

    public void setCodigoTransacao(UUID codigoTransacao) {
        this.codigoTransacao = codigoTransacao;
    }

    public void setCodigoTransacao(String codigoTransacao) {
        this.codigoTransacao = UUID.fromString(codigoTransacao);
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
        result = prime * result + ((valor == null) ? 0 : valor.hashCode());
        result = prime * result + ((formaPagamento == null) ? 0 : formaPagamento.hashCode());
        result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
        result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
        result = prime * result + ((dataAtualizacao == null) ? 0 : dataAtualizacao.hashCode());
        result = prime * result + ((codigoTransacao == null) ? 0 : codigoTransacao.hashCode());
        result = prime * result + ((detalhes == null) ? 0 : detalhes.hashCode());
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
        Pagamento other = (Pagamento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (pedido == null) {
            if (other.pedido != null)
                return false;
        } else if (!pedido.equals(other.pedido))
            return false;
        if (valor == null) {
            if (other.valor != null)
                return false;
        } else if (!valor.equals(other.valor))
            return false;
        if (formaPagamento != other.formaPagamento)
            return false;
        if (situacao != other.situacao)
            return false;
        if (dataCriacao == null) {
            if (other.dataCriacao != null)
                return false;
        } else if (!dataCriacao.equals(other.dataCriacao))
            return false;
        if (dataAtualizacao == null) {
            if (other.dataAtualizacao != null)
                return false;
        } else if (!dataAtualizacao.equals(other.dataAtualizacao))
            return false;
        if (codigoTransacao == null) {
            if (other.codigoTransacao != null)
                return false;
        } else if (!codigoTransacao.equals(other.codigoTransacao))
            return false;
        if (detalhes == null) {
            if (other.detalhes != null)
                return false;
        } else if (!detalhes.equals(other.detalhes))
            return false;
        return true;
    }

}
