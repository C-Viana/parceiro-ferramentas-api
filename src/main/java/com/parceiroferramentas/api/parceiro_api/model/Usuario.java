package com.parceiroferramentas.api.parceiro_api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String username;

    @Column(name = "senha")
    private String password;
    private boolean account_non_expired;
    private boolean account_non_locked;
    private boolean credentials_non_expired;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "acesso",
        joinColumns = {@JoinColumn(name = "id_usuario")},
        inverseJoinColumns = {@JoinColumn(name = "id_permissao")}
    )
    private List<Permissao> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EnderecoUsuario> enderecos = new ArrayList<>();

    public Usuario(){}

    public Usuario(String username, String nome, String password, List<Permissao> permissoes) {
        this.username = username;
        this.nome = nome;
        this.password = password;
        this.authorities = permissoes;
    }

    public Usuario(Long id, String username, String nome, String password, boolean account_non_expired,
            boolean account_non_locked, boolean credentials_non_expired, boolean enabled, List<Permissao> permissoes, List<EnderecoUsuario> enderecos) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.password = password;
        this.account_non_expired = account_non_expired;
        this.account_non_locked = account_non_locked;
        this.credentials_non_expired = credentials_non_expired;
        this.enabled = enabled;
        this.authorities = permissoes;
        this.enderecos = enderecos;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List<Permissao> permissoes = new ArrayList<>();
        // for (PerfisAcesso p : this.authorities.stream().map(Permissao::getEnum).toList()) {
        //     permissoes.add(new Permissao(null, p.getEnum()));
        // }
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.account_non_expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.account_non_locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentials_non_expired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    // ------------ JPA ------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccount_non_expired(boolean account_non_expired) {
        this.account_non_expired = account_non_expired;
    }

    public void setAccount_non_locked(boolean account_non_locked) {
        this.account_non_locked = account_non_locked;
    }

    public void setCredentials_non_expired(boolean credentials_non_expired) {
        this.credentials_non_expired = credentials_non_expired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(List<Permissao> authorities) {
        this.authorities = authorities;
    }

    public List<EnderecoUsuario> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoUsuario> enderecos) {
        this.enderecos = enderecos;
    }

    public void adicionarEndereco(EnderecoUsuario endereco) {
        this.enderecos.add(endereco);
        endereco.setUsuario(this);
    }

    public void removerEndereco(EnderecoUsuario endereco) {
        this.enderecos.remove(endereco);
        endereco.setUsuario(null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + (account_non_expired ? 1231 : 1237);
        result = prime * result + (account_non_locked ? 1231 : 1237);
        result = prime * result + (credentials_non_expired ? 1231 : 1237);
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
        result = prime * result + ((enderecos == null) ? 0 : enderecos.hashCode());
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
        Usuario other = (Usuario) obj;
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
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (account_non_expired != other.account_non_expired)
            return false;
        if (account_non_locked != other.account_non_locked)
            return false;
        if (credentials_non_expired != other.credentials_non_expired)
            return false;
        if (enabled != other.enabled)
            return false;
        if (authorities == null) {
            if (other.authorities != null)
                return false;
        } else if (!authorities.equals(other.authorities))
            return false;
        if (enderecos == null) {
            if (other.enderecos != null)
                return false;
        } else if (!enderecos.equals(other.enderecos))
            return false;
        return true;
    }
    
}
