package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;

@Component
public class DebitoStrategy implements PagamentoStrategy {

    @Override
    public Pagamento processar(Pedido pedido, String detalhes) {
        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento(TIPO_PAGAMENTO.DEBITO);
        pagamento.setSituacao(STATUS_PAGAMENTO.APROVADO);
        pagamento.setCodigoTransacao(UUID.randomUUID());
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setDetalhes(detalhes);
        return pagamento;
    }

}
