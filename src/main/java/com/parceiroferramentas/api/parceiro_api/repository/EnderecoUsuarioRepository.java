package com.parceiroferramentas.api.parceiro_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parceiroferramentas.api.parceiro_api.model.Endereco;

@Repository
public interface EnderecoUsuarioRepository extends JpaRepository<Endereco, Long> {

    public List<Endereco> findEnderecoByUsuarioId(Long id);

}
