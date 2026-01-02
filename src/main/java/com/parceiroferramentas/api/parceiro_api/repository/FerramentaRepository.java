package com.parceiroferramentas.api.parceiro_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;


public interface FerramentaRepository extends JpaRepository<Ferramenta, Long> {

    public Page<Ferramenta> findByTipoEqualsIgnoreCase(String tipo, Pageable pageable);
}
