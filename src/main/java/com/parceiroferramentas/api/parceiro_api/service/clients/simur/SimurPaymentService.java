package com.parceiroferramentas.api.parceiro_api.service.clients.simur;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentRequest;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimurPaymentService {

    private RestClient customClient;
    // private static final String SIMUR_BASE_URL = System.getProperty("SIMUR_HOSTNAME");
    private static final String SIMUR_BASE_URL = "https://localhost:7033/api";

    public SimurPaymentService() {
        try {
            customClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Authorization", new SimurAuthenticationService().getBearerToken())
                .baseUrl(SIMUR_BASE_URL)
                .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public SimurPaymentResponse criarPagamento(SimurPaymentRequest request) throws JsonProcessingException {
        String stringResponseBody = customClient.post().uri("/v1/payment").body(request).retrieve().body(String.class);
        SimurPaymentResponse result = new ObjectMapper().readValue(stringResponseBody, SimurPaymentResponse.class);

        if(result == null)
            throw new RuntimeException("Payment could not be created at Simur Pagamentos");

        return result;
    }
}
