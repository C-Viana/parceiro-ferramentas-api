package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;

public interface PagamentoStrategy {
    Pagamento processar(Pedido pedido);
}
