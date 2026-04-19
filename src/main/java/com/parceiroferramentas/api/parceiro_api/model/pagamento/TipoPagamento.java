package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.util.HashMap;
import java.util.Map;

public enum TipoPagamento {
    PIX_DYNAMIC("PIX_DYNAMIC"),
    CARTAO_CREDITO("CREDIT_CARD"),
    BOLETO("BOLETO"),
    DINHEIRO("CASH"),
    DEBITO("DEBIT_CARD");

    private String tipoValue;
    private static final Map<String, TipoPagamento> LOOKUP = new HashMap<>();

    TipoPagamento(String tipo) {
        this.tipoValue = tipo;
    }

    public String getStringValue() {
        return tipoValue;
    }

    static {
        for (TipoPagamento tipo : TipoPagamento.values()) {
            LOOKUP.put(tipo.tipoValue, tipo);
        }
    }

    public static TipoPagamento getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
