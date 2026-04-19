package com.parceiroferramentas.api.parceiro_api.enums;

public enum PerfilAcesso {
    ADMIN("ADMIN"),
    GERENTE("GERENTE"),
    VENDEDOR("VENDEDOR"),
    CLIENTE("CLIENTE");

    private String role;

    PerfilAcesso(String role) {
        this.role = role;
    }

    public PerfilAcesso getEnum() {
        return this;
    }

    public String getString() {
        return role;
    }
}
