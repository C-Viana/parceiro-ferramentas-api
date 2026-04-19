package com.parceiroferramentas.api.parceiro_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parceiroferramentas.api.parceiro_api.model.Comprador;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, UUID> {
    Optional<Comprador> findCompradorByDocumento(String documento);
}
