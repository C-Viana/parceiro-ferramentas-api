package com.parceiroferramentas.api.parceiro_api.enums;

import java.util.HashMap;
import java.util.Map;

public enum Estados {
    ACRE ("Acre"),
    ALAGOAS ("Alagoas"),
    AMAZONAS ("Amazonas"),
    AMAPA ("Amapá"),
    BAHIA ("Bahia"),
    CEARA ("Ceará"),
    DISTRITO_FEDERAL ("Distrito Federal"),
    ESPIRITO_SANTO ("Espírito Santo"),
    GOIAS ("Goiás"),
    MARANHAO ("Maranhão"),
    MINAS_GERAIS ("Minas Gerais"),
    MATO_GROSSO_DO_SUL ("Mato Grosso do Sul"),
    MATO_GROSSO ("Mato Grosso"),
    PARA ("Pará"),
    PARAIBA ("Paraíba"),
    PERNAMBUCO ("Pernambuco"),
    PIAUI ("Piauí"),
    PARANA ("Paraná"),
    RIO_DE_JANEIRO ("Rio de Janeiro"),
    RIO_GRANDE_DO_NORTE ("Rio Grande do Norte"),
    RONDONIA ("Rondônia"),
    RORAIMA ("Roraima"),
    RIO_GRANDE_DO_SUL ("Rio Grande do Sul"),
    SANTA_CATARINA ("Santa Catarina"),
    SERGIPE ("Sergipe"),
    SAO_PAULO ("São Paulo"),
    TOCANTINS ("Tocantins ");

    private String ufSigla;
    private static final Map<String, Estados> LOOKUP = new HashMap<>();

    Estados(String role) {
        this.ufSigla = role;
    }

    public Estados getEnum() {
        return this;
    }

    public String getString() {
        return ufSigla;
    }

    static {
        for (Estados estado : Estados.values()) {
            LOOKUP.put(estado.ufSigla, estado);
        }
    }

    public static Estados getByDisplayValue(String displayValue) {
        return LOOKUP.get(displayValue);
    }
}
