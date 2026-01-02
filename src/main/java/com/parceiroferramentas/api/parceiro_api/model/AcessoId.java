package com.parceiroferramentas.api.parceiro_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AcessoId {

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_permissao")
    private Long idPermissao;

    public AcessoId() {}

    public AcessoId(Long idUsuario, Long idPermissao) {
        this.idUsuario = idUsuario;
        this.idPermissao = idPermissao;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Long idPermissao) {
        this.idPermissao = idPermissao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
        result = prime * result + ((idPermissao == null) ? 0 : idPermissao.hashCode());
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
        AcessoId other = (AcessoId) obj;
        if (idUsuario == null) {
            if (other.idUsuario != null)
                return false;
        } else if (!idUsuario.equals(other.idUsuario))
            return false;
        if (idPermissao == null) {
            if (other.idPermissao != null)
                return false;
        } else if (!idPermissao.equals(other.idPermissao))
            return false;
        return true;
    }

    

}
