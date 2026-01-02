package com.parceiroferramentas.api.parceiro_api.enums;

public enum PerfisAcesso {
    ADMIN("ADMIN"),
    GERENTE("GERENTE"),
    VENDEDOR("VENDEDOR"),
    CLIENTE("CLIENTE");

    private String role;

    PerfisAcesso(String role) {
        this.role = role;
    }

    public PerfisAcesso getEnum() {
        return this;
    }

    public String getString() {
        return role;
    }
}
