package com.parceiroferramentas.api.parceiro_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parceiroferramentas.api.parceiro_api.model.ItemCarrinho;

@Repository
public interface CarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    List<ItemCarrinho> findItemCarrinhoByUsuarioId(Long usuarioId);
}
