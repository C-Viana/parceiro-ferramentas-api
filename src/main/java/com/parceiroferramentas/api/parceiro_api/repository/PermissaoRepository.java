package com.parceiroferramentas.api.parceiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;
import com.parceiroferramentas.api.parceiro_api.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    Permissao findPermissaoByAuthority(PerfisAcesso authority);
}
