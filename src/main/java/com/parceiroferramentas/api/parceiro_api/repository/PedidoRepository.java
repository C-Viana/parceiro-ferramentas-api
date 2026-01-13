package com.parceiroferramentas.api.parceiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
