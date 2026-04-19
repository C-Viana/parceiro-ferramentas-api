package com.parceiroferramentas.api.parceiro_api.model.pedido;

import java.util.HashMap;
import java.util.Map;

public enum StatusPedido {
    //ALUGUEL E COMPRA
    CRIADO("CRIADO"), 
    PROCESSANDO("PROCESSANDO"), 
    PENDENTE("PENDENTE"), 
    FINALIZADO("FINALIZADO"), 
    CANCELADO("CANCELADO"), 
    DEVOLVIDO("DEVOLVIDO"), 
    ESTRAVIADO("ESTRAVIADO"), 
    ATRASADO("ATRASADO"),
    REEMBOLSADO("REEMBOLSADO"), 

    //VENDA
    EM_ROTA("EM_ROTA"), 
    PAGAMENTO_RECUSADO("PAGAMENTO_RECUSADO"), 
    RECUSADO_CLIENTE("RECUSADO_CLIENTE"), 

    //ALUGUEL
    RETIDO("RETIDO"), 
    RESERVADO("RESERVADO"), 
    ALUGADO("ALUGADO"), 
    BLOQUEADO("BLOQUEADO"), 
    PENDENCIA_CLIENTE("PENDENCIA_CLIENTE");

    private String value;
    private static final  Map<String, StatusPedido> LOOKUP = new HashMap<>();

    StatusPedido(String value) {
        this.value = value;
    }

    public String getString() {
        return value;
    }

    static {
        for (StatusPedido status : StatusPedido.values()) {
            LOOKUP.put(status.getString(), status);
        }
    }

    public static StatusPedido getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
