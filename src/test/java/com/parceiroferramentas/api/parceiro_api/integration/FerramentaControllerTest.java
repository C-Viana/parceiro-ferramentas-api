package com.parceiroferramentas.api.parceiro_api.integration;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.parceiroferramentas.api.parceiro_api.data.CreateIntegratedData;
import com.parceiroferramentas.api.parceiro_api.data.CreateMockedData;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.model.Ferramenta;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FerramentaControllerTest {

    static RestAssuredConfig config;

    public static final String BASE_PATH = "http://localhost:8080";
    public static final List<Ferramenta> ferramentasMock = CreateMockedData.getInstance().getFerramentas();

    @BeforeAll
    static void setUp() {
        config = RestAssured.config();
        config.getEncoderConfig().defaultContentCharset("UTF-8");
        RestAssured.config = config;
        RestAssured.baseURI = BASE_PATH;
    }

    public String getAccessToken(CredenciaisUsuarioDto credencial) {
        Response res = RestAssured
            .given()
                .contentType("application/json")
                .body(credencial)
            .when()
                .post("/usuarios/signin");
        return res.body().jsonPath().getString("acesso");
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar uma nova ferramenta")
    void salvarFerramenta() {
        Ferramenta ferramenta = CreateMockedData.getInstance().getNovaFerramenta();
        String token = getAccessToken(CreateIntegratedData.getInstance().getCredenciais().get("admin"));

        Response res = 
            RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+token)
                .body(ferramenta)
            .when()
                .post("/api/v1/ferramentas");
        
        Assertions.assertEquals(201, res.getStatusCode());
        Assertions.assertNotNull(res.body().jsonPath().getLong("id"));
        Assertions.assertTrue(res.body().jsonPath().getLong("id") > 0);
        Assertions.assertEquals(ferramenta.getNome(), res.body().jsonPath().getString("nome"));
    }

}
