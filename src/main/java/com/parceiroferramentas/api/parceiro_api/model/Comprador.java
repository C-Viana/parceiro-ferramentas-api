package com.parceiroferramentas.api.parceiro_api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Comprador {

    @Id
    //@GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?:\\d{11}|\\d{14})$")
    private String documento;

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate nascimento;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @Pattern(regexp = "\\+?[0-9]{10,13}")
    @NotNull
    @NotBlank
    private String phone;
    
    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemCarrinho> carrinhoItens = new ArrayList<>();

    public Comprador() {}

    public Comprador(String documento, String nome, LocalDate nascimento, @Email String email,
            @Pattern(regexp = "\\+?[0-9]{10,13}") String phone, List<Endereco> enderecos) {
        this.documento = documento;
        this.nome = nome;
        this.nascimento = nascimento;
        this.email = email;
        this.phone = phone;
        this.enderecos = enderecos;
    }

    public Comprador(String documento, String nome, LocalDate nascimento, @Email String email,
            @Pattern(regexp = "\\+?[0-9]{10,13}") String phone, List<Endereco> enderecos,
            List<ItemCarrinho> carrinhoItens) {
        this.documento = documento;
        this.nome = nome;
        this.nascimento = nascimento;
        this.email = email;
        this.phone = phone;
        this.enderecos = enderecos;
        this.carrinhoItens = carrinhoItens;
    }

    public Comprador(UUID id, String documento, String nome, LocalDate nascimento, @Email String email,
            @Pattern(regexp = "\\+?[0-9]{10,13}") String phone, List<Endereco> enderecos,
            List<ItemCarrinho> carrinhoItens) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.documento = documento;
        this.nome = nome;
        this.nascimento = nascimento;
        this.email = email;
        this.phone = phone;
        this.enderecos = enderecos;
        this.carrinhoItens = carrinhoItens;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public void adicionarEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setComprador(this);
    }

    public void removerEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setComprador(null);
    }

    public List<ItemCarrinho> getCarrinhoItens() {
        return carrinhoItens;
    }

    public void setCarrinhoItens(List<ItemCarrinho> carrinhoItens) {
        this.carrinhoItens = carrinhoItens;
    }

    

    public void adicionarAoCarrinho(Ferramenta ferramenta, Integer quantidade) {
        if (ferramenta == null) {
            throw new IllegalArgumentException("Ferramenta não pode ser nula");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        Optional<ItemCarrinho> itemExistente = carrinhoItens.stream()
                .filter(item -> item.getFerramenta().equals(ferramenta))
                .findFirst();
        
        if (itemExistente.isPresent()) {
            ItemCarrinho item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
        } else {
            ItemCarrinho novoItem = new ItemCarrinho(this, ferramenta, quantidade);
            carrinhoItens.add(novoItem);
        }
    }

    public void removerDoCarrinho(Ferramenta ferramenta) {
        if (ferramenta == null) {
            throw new IllegalArgumentException("Ferramenta não pode ser nula");
        }
        carrinhoItens.removeIf(item -> item.getFerramenta().equals(ferramenta));
    }

    public void mudarQuantidade(ItemCarrinho item, Integer novaQuantidade) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (!carrinhoItens.contains(item)) {
            throw new IllegalArgumentException("Item não pertence ao carrinho deste usuário");
        }

        item.setQuantidade(novaQuantidade);
    }

    public void limparCarrinho() {
        carrinhoItens.clear();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documento == null) ? 0 : documento.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((nascimento == null) ? 0 : nascimento.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
        Comprador other = (Comprador) obj;
        if (documento == null) {
            if (other.documento != null)
                return false;
        } else if (!documento.equals(other.documento))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (nascimento == null) {
            if (other.nascimento != null)
                return false;
        } else if (!nascimento.equals(other.nascimento))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        return true;
    }

}
