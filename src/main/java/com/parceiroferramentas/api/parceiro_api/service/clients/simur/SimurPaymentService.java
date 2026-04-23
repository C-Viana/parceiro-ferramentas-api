package com.parceiroferramentas.api.parceiro_api.service.clients.simur;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentRequest;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimurPaymentService {

    private RestClient customClient;
    private Builder builder = RestClient.builder();
    private static final String SIMUR_BASE_URL = System.getenv("SIMUR_HOSTNAME");

    public SimurPaymentService() {}

    public SimurPaymentResponse criarPagamento(SimurPaymentRequest request) throws JsonProcessingException {
        String token = new SimurAuthenticationService(builder).getBearerToken();

        customClient = builder
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Accept", "application/json")
            .defaultHeader("Authorization", token)
            .baseUrl(SIMUR_BASE_URL)
            .build();
        
        String stringResponseBody = customClient.post().uri("/api/v1/payment").body(request).retrieve().body(String.class);
        SimurPaymentResponse result = new ObjectMapper().readValue(stringResponseBody, SimurPaymentResponse.class);

        if(result == null)
            throw new RuntimeException("Payment could not be created at Simur Pagamentos");

        return result;
    }
}
