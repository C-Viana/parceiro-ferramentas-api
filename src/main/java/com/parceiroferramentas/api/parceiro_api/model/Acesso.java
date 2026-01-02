package com.parceiroferramentas.api.parceiro_api.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "acesso")
public class Acesso {

    @EmbeddedId
    private AcessoId id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @MapsId("idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_permissao")
    @MapsId("idPermissao")
    private Permissao permissao;

    public Acesso() {}

    public Acesso(Usuario usuario, Permissao permissao) {
        this.usuario = usuario;
        this.permissao = permissao;
        this.id = new AcessoId(usuario.getId(), permissao.getId());
    }

    public AcessoId getId() {
        return id;
    }

    public void setId(AcessoId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

}
