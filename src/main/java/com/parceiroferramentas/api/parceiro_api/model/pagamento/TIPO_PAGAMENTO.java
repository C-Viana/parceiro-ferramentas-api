package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.util.HashMap;
import java.util.Map;

public enum TIPO_PAGAMENTO {
    PIX("PIX"),
    CARTAO_CREDITO("CARTAO_CREDITO"),
    BOLETO("BOLETO"),
    DINHEIRO("DINHEIRO"),
    DEBITO("DEBITO");

    private String tipoValue;
    private static final Map<String, TIPO_PAGAMENTO> LOOKUP = new HashMap<>();

    TIPO_PAGAMENTO(String tipo) {
        this.tipoValue = tipo;
    }

    public String getStringValue() {
        return tipoValue;
    }

    static {
        for (TIPO_PAGAMENTO tipo : TIPO_PAGAMENTO.values()) {
            LOOKUP.put(tipo.tipoValue, tipo);
        }
    }

    public static TIPO_PAGAMENTO getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
