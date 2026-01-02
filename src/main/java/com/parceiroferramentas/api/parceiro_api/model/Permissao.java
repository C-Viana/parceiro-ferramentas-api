package com.parceiroferramentas.api.parceiro_api.model;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Permissao implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "descricao")
    private PerfisAcesso authority;
    

    public Permissao() {
    }

    public Permissao(Long id, PerfisAcesso authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setAuthority(PerfisAcesso authority) {
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return this.authority.getString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((authority == null) ? 0 : authority.hashCode());
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
        Permissao other = (Permissao) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (authority == null) {
            if (other.authority != null)
                return false;
        } else if (!authority.equals(other.authority))
            return false;
        return true;
    }

}
