package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.util.HashMap;
import java.util.Map;

public enum StatusPagamento {
    CRIADO("CREATED"), 
    PENDENTE("PENDING"), 
    PROCESSANDO("PROCESSING"), 
    EFETIVADO("CAPTURED"), 
    APROVADO("SETLED"), 
    RECUSADO("REJECTED"), 
    CANCELADO("CANCELLED"), 
    REEMBOLSADO("REFUNDED");

    private String statusValue;
    private static final Map<String, StatusPagamento> LOOKUP = new HashMap<>();

    StatusPagamento(String status) {
        this.statusValue = status;
    }

    public String getStringValue() {
        return statusValue;
    }

    static {
        for (StatusPagamento tipo : StatusPagamento.values()) {
            LOOKUP.put(tipo.statusValue, tipo);
        }
    }

    public static StatusPagamento getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
