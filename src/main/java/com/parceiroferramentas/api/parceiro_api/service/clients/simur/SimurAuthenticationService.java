package com.parceiroferramentas.api.parceiro_api.service.clients.simur;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimurAuthenticationService {

    private RestClient customClient;
    // private static final String SIMUR_BASE_URL = System.getProperty("SIMUR_HOSTNAME");
    // private static final String USERNAME = System.getProperty("SIMUR_USERNAME");
    // private static final String PASSWORD = System.getProperty("SIMUR_PASSWORD");

    private static final String SIMUR_BASE_URL = "https://localhost:7033/api";
    private static final String USERNAME = "parceiroferramentas";
    private static final String PASSWORD = "parceiroferramentas";

    public SimurAuthenticationService() {
        customClient = RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl(SIMUR_BASE_URL)
            .defaultHeader("Content-Type", "application/json")
            //.defaultUriVariables(Map.of("variable", "foo"))
            //.defaultHeader("My-Header", "Foo")
            //.defaultCookie("My-Cookie", "Bar")
            //.defaultVersion("1.2")
            //.apiVersionInserter(ApiVersionInserter.fromHeader("API-Version").build())
            //.requestInterceptor(myCustomInterceptor)
            //.requestInitializer(myCustomInitializer)
            .build();
    }

    public String getBearerToken() throws JsonProcessingException {
        String body = "{\r\n" + //
                        "    \"username\": \""+USERNAME+"\",\r\n" + //
                        "    \"password\": \""+PASSWORD+"\"\r\n" + //
                        "}";
        String result = customClient.post().uri("/auth/signin").body(body).retrieve().body(String.class);
        String token = "";
        try {
            token = new ObjectMapper().readTree(result).get("access_token").asText();
        } catch (JsonProcessingException ex) {
            throw ex;
        }
        return "Bearer " + token;
    }
}
