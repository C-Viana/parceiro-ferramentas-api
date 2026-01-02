package com.parceiroferramentas.api.parceiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parceiroferramentas.api.parceiro_api.model.Acesso;
import com.parceiroferramentas.api.parceiro_api.model.AcessoId;

@Repository
public interface AcessoRepository extends JpaRepository<Acesso, AcessoId> {

}
