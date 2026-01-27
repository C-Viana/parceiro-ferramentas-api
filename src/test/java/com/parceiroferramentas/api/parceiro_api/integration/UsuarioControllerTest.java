package com.parceiroferramentas.api.parceiro_api.integration;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.parceiroferramentas.api.parceiro_api.data.CreateIntegratedData;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.PermissaoRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioRequestDto;
import com.parceiroferramentas.api.parceiro_api.enums.PERFIL_ACESSO;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

    static RestAssuredConfig config;

    static List<Usuario> usuariosMock;
    static final String BASE_PATH = "http://localhost:8080/usuarios";
    static Map<String, CredenciaisUsuarioDto> credenciais = null;
    static String refreshToken = null;

    private static Stream<Arguments> provideTestData() {
        return credenciais.entrySet().stream()
                .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    @BeforeAll
    static void setUp() {
        config = RestAssured.config();
        config.getEncoderConfig().defaultContentCharset("UTF-8");
        RestAssured.config = config;
        RestAssured.baseURI = BASE_PATH;
        credenciais = CreateIntegratedData.getInstance().getCredenciais();
    }

    @ParameterizedTest
    @Order(1)
    @MethodSource("provideTestData")
    @DisplayName("Deve logar com sucesso usando um usuário válido")
    void logarUsuario(String perfil, CredenciaisUsuarioDto credencial) {
        Response res = RestAssured.given()
            .contentType("application/json")
            .body(credencial)
            .when()
            .post("/signin");
        
        Assertions.assertEquals(200, res.getStatusCode());
        Assertions.assertNotNull(res.body().jsonPath().getString("acesso"));
        Assertions.assertEquals(credenciais.get(perfil).username(), res.body().jsonPath().getString("username"));
    }

    @ParameterizedTest
    @Order(2)
    @MethodSource("provideTestData")
    @DisplayName("Deve renovar o acesso de um usuário válido")
    void renovarAcessoUsuarioAdmin(String perfil, CredenciaisUsuarioDto credencial) {
        refreshToken = RestAssured.given()
            .contentType("application/json")
            .body(credencial)
            .when()
            .post("/signin")
            .body().jsonPath()
            .getString("renovacao");
        
        Response res = RestAssured.given()
            .contentType("application/json")
            .header("Authorization", "Bearer "+refreshToken)
            .when()
            .log().all()
            .put("/refresh/" + credencial.username());
        
        Assertions.assertEquals(200, res.getStatusCode());
        Assertions.assertNotNull(res.body().jsonPath().getString("acesso"));
        Assertions.assertEquals(credencial.username(), res.body().jsonPath().getString("username"));
    }

    @Test
    @Order(3)
    @DisplayName("Deve cadastrar um novo cliente com sucesso")
    void cadastrarNovoClienteSucesso() {
        String nomeCliente = "Cliente Teste "+System.currentTimeMillis();
        String username = "CLIE"+nomeCliente.substring(nomeCliente.length()-4);

        Response res = RestAssured.given()
            .contentType("application/json")
            .body(new UsuarioRequestDto(null, nomeCliente, username, "12345678", true, null))
            .when()
            .post("/signup");
        
        Assertions.assertEquals(201, res.getStatusCode());
        Assertions.assertNull(res.body().jsonPath().getString("senha"));
        Assertions.assertEquals(username, res.body().jsonPath().getString("username"));
        Assertions.assertEquals(nomeCliente, res.body().jsonPath().getString("nome"));
    }

    @Test
    @Order(4)
    @DisplayName("Deve cadastrar um novo gerente com sucesso")
    void cadastrarNovoGerenteSucesso() {
        String nomeGerente = "Gerente Teste "+System.currentTimeMillis();
        String username = "GERE"+nomeGerente.substring(nomeGerente.length()-4);

        Response res = RestAssured.given()
            .contentType("application/json")
            .body(new UsuarioRequestDto(null, nomeGerente, username, "12345678", true, List.of(new PermissaoRequestDto(PERFIL_ACESSO.GERENTE.toString()))))
            .when()
            .post("/signup");
        
        Assertions.assertEquals(201, res.getStatusCode());
        Assertions.assertNull(res.body().jsonPath().getString("senha"));
        Assertions.assertEquals(username, res.body().jsonPath().getString("username"));
        Assertions.assertEquals(nomeGerente, res.body().jsonPath().getString("nome"));
    }

}
