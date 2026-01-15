package com.parceiroferramentas.api.parceiro_api.model.pedido;

import java.util.HashMap;
import java.util.Map;

public enum STATUS_PEDIDO {
    //ALUGUEL E COMPRA
    PENDENTE("PENDENTE"), 
    APROVADO("APROVADO"), 
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
    private static final  Map<String, STATUS_PEDIDO> LOOKUP = new HashMap<>();

    STATUS_PEDIDO(String value) {
        this.value = value;
    }

    public String getString() {
        return value;
    }

    static {
        for (STATUS_PEDIDO status : STATUS_PEDIDO.values()) {
            LOOKUP.put(status.getString(), status);
        }
    }

    public static STATUS_PEDIDO getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
