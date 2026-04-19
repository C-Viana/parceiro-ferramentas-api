package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.parceiroferramentas.api.parceiro_api.enums.ParceiroFerramentasInfo;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.SimurPaymentService;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.CreditCardDetails;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentRequest;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentResponse;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class CreditoStrategy implements PagamentoStrategy {
    private final SimurPaymentService simurClient = new SimurPaymentService();

    @Override
    public Pagamento processar(Pedido pedido, String detalhesPagamento) {
        String dataPedido = DateTimeFormatter.ofPattern("yyyyMMddhhmm").format(LocalDateTime.now(ZoneOffset.systemDefault()));
        JsonNode jsonPayload = new ObjectMapper().readTree(detalhesPagamento);

        SimurPaymentRequest request = new SimurPaymentRequest(
            "PF-" + dataPedido + "-" + pedido.getId(),             // external_order_id
            pedido.getValorTotal(),
            "BRL",
            ParceiroFerramentasInfo.LOJA_DOCUMENTO.getString(),
            pedido.getComprador().getDocumento(),
            new CreditCardDetails(
                jsonPayload.get("installments").asInt(),
                jsonPayload.get("card_token").asString()
            )
        );

        SimurPaymentResponse response;
        try {
            response = simurClient.criarPagamento(request);
            String detalhesJsonString = new ObjectMapper().writeValueAsString(response.getPaymentDetails());

            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(TipoPagamento.CARTAO_CREDITO);
            pagamento.setValor(response.getAmount());
            pagamento.setSituacao(StatusPagamento.getByDisplayValue(response.getStatus()));
            pagamento.setDataCriacao(Instant.now());
            pagamento.setDataAtualizacao(Instant.now());
            pagamento.setCodigoTransacao(UUID.fromString(response.getId()));
            pagamento.setDetalhes(detalhesJsonString);
            return pagamento;
        } catch (Exception e) {
            return null;
        }
    }

}
