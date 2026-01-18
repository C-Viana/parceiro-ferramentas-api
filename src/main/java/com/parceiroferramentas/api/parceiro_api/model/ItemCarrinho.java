package com.parceiroferramentas.api.parceiro_api.model;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrinho_itens")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ferramenta_id", nullable = false)
    private Ferramenta ferramenta;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_aluguel_momento", precision = 10, scale = 2)
    private BigDecimal precoAluguelMomento;

    @Column(name = "preco_venda_momento", precision = 10, scale = 2)
    private BigDecimal precoVendaMomento;

    @Column(name = "url_imagem")
    private String urlImage;

    @Column(name = "data_adicao")
    private Instant dataAdicao = Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo")));
    
    public ItemCarrinho() {}

    public ItemCarrinho(Usuario usuario, Ferramenta ferramenta, Integer quantidade) {
        this.usuario = usuario;
        this.ferramenta = ferramenta;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ferramenta getFerramenta() {
        return ferramenta;
    }

    public void setFerramenta(Ferramenta ferramenta) {
        this.ferramenta = ferramenta;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoVendaMomento() {
        return precoVendaMomento;
    }

    public void setPrecoVendaMomento(BigDecimal precoCompraMomento) {
        this.precoVendaMomento = precoCompraMomento;
    }

    public BigDecimal getPrecoAluguelMomento() {
        return precoAluguelMomento;
    }

    public void setPrecoAluguelMomento(BigDecimal precoAluguelMomento) {
        this.precoAluguelMomento = precoAluguelMomento;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        if(urlImage == null && ferramenta != null)
            this.urlImage = ferramenta.getLista_imagens().get(0);
        this.urlImage = urlImage;
    }

    public Instant getDataAdicao() {
        return dataAdicao;
    }

    public void setDataAdicao(Instant dataAdicao) {
        this.dataAdicao = dataAdicao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
        result = prime * result + ((ferramenta == null) ? 0 : ferramenta.hashCode());
        result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
        result = prime * result + ((precoVendaMomento == null) ? 0 : precoVendaMomento.hashCode());
        result = prime * result + ((precoAluguelMomento == null) ? 0 : precoAluguelMomento.hashCode());
        result = prime * result + ((urlImage == null) ? 0 : urlImage.hashCode());
        result = prime * result + ((dataAdicao == null) ? 0 : dataAdicao.hashCode());
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
        ItemCarrinho other = (ItemCarrinho) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (usuario == null) {
            if (other.usuario != null)
                return false;
        } else if (!usuario.equals(other.usuario))
            return false;
        if (ferramenta == null) {
            if (other.ferramenta != null)
                return false;
        } else if (!ferramenta.equals(other.ferramenta))
            return false;
        if (quantidade == null) {
            if (other.quantidade != null)
                return false;
        } else if (!quantidade.equals(other.quantidade))
            return false;
        if (precoVendaMomento == null) {
            if (other.precoVendaMomento != null)
                return false;
        } else if (!precoVendaMomento.equals(other.precoVendaMomento))
            return false;
        if (precoAluguelMomento == null) {
            if (other.precoAluguelMomento != null)
                return false;
        } else if (!precoAluguelMomento.equals(other.precoAluguelMomento))
            return false;
        if (urlImage == null) {
            if (other.urlImage != null)
                return false;
        } else if (!urlImage.equals(other.urlImage))
            return false;
        if (dataAdicao == null) {
            if (other.dataAdicao != null)
                return false;
        } else if (!dataAdicao.equals(other.dataAdicao))
            return false;
        return true;
    }

}
