package com.parceiroferramentas.api.parceiro_api.service.clients.simur;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimurAuthenticationService {

    private RestClient customClient;
    private static final String SIMUR_BASE_URL = System.getenv("SIMUR_HOSTNAME");
    private static final String USERNAME = System.getenv("SIMUR_USERNAME");
    private static final String PASSWORD = System.getenv("SIMUR_PASSWORD");

    public SimurAuthenticationService(RestClient.Builder builder) {
        customClient = builder
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl(SIMUR_BASE_URL)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public String getBearerToken() throws JsonProcessingException {
        String body = "{\r\n" + //
                        "    \"username\": \""+USERNAME+"\",\r\n" + //
                        "    \"password\": \""+PASSWORD+"\"\r\n" + //
                        "}";
        String result = customClient.post().uri("/api/auth/signin").body(body).retrieve().body(String.class);
        String token = "";
        try {
            token = new ObjectMapper().readTree(result).get("access_token").asText();
        } catch (JsonProcessingException ex) {
            throw ex;
        }
        return "Bearer " + token;
    }
}
