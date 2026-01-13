package com.parceiroferramentas.api.parceiro_api.enums;

public enum PERFIL_ACESSO {
    ADMIN("ADMIN"),
    GERENTE("GERENTE"),
    VENDEDOR("VENDEDOR"),
    CLIENTE("CLIENTE");

    private String role;

    PERFIL_ACESSO(String role) {
        this.role = role;
    }

    public PERFIL_ACESSO getEnum() {
        return this;
    }

    public String getString() {
        return role;
    }
}
