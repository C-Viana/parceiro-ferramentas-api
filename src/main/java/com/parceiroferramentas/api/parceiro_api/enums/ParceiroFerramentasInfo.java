package com.parceiroferramentas.api.parceiro_api.enums;

import java.util.HashMap;
import java.util.Map;

public enum ParceiroFerramentasInfo {
    LOJA_DOCUMENTO ("53178402000147"),
    LOJA_NOME ("Parceiro Ferramentas Ltda"),
    BANCO ("0237"),
    AGENCIA ("3051"),
    CONTA ("0790052-9"),
    PIX_DESCRIPTION ("Compra na Parceiro Ferramentas Ltda");

    private String info;
    private static final Map<String, ParceiroFerramentasInfo> LOOKUP = new HashMap<>();

    ParceiroFerramentasInfo(String info) {
        this.info = info;
    }

    public ParceiroFerramentasInfo getEnum() {
        return this;
    }

    public String getString() {
        return info;
    }

    static {
        for (ParceiroFerramentasInfo estado : ParceiroFerramentasInfo.values()) {
            LOOKUP.put(estado.info, estado);
        }
    }

    public static ParceiroFerramentasInfo getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
