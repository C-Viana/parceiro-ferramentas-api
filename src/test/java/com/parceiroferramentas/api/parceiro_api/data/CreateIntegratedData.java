package com.parceiroferramentas.api.parceiro_api.data;

import java.util.HashMap;
import java.util.Map;

import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;

public class CreateIntegratedData {

    //private File json = new File("src/test/resources/response_success.json");

    public static CreateIntegratedData getInstance() {
        return new CreateIntegratedData();
    }

    public Map<String, CredenciaisUsuarioDto> getCredenciais() {
        Map<String, CredenciaisUsuarioDto> credenciais = new HashMap<>();
        credenciais.put("admin", new CredenciaisUsuarioDto("80690571", "admin123"));
        credenciais.put("gerente", new CredenciaisUsuarioDto("GERE0001", "admin123"));
        credenciais.put("vendedor", new CredenciaisUsuarioDto("VEND0001", "admin123"));
        credenciais.put("cliente", new CredenciaisUsuarioDto("CLIE0001", "admin123"));
        return credenciais;
    }

}
