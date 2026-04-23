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
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.PixDynamicRequestDetails;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentRequest;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentResponse;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class PixStrategy implements PagamentoStrategy {
    
    private final SimurPaymentService simurClient;

    public PixStrategy(SimurPaymentService simurClient) {
        this.simurClient = simurClient;
    }

    @Override
    public Pagamento processar(Pedido pedido, String detalhesPagamento) {
        String dataPedido = DateTimeFormatter.ofPattern("yyyyMMddhhmm").format(LocalDateTime.now(ZoneOffset.systemDefault()));

        SimurPaymentRequest request = new SimurPaymentRequest(
            "PF-" + dataPedido + "-" + pedido.getId(),             // external_order_id
            pedido.getValorTotal(),
            "BRL",
            ParceiroFerramentasInfo.LOJA_DOCUMENTO.getString(),
            pedido.getComprador().getDocumento(),
            new PixDynamicRequestDetails(
                ParceiroFerramentasInfo.PIX_DESCRIPTION.getString()
            )
        );

        SimurPaymentResponse response;
        String detalhesJsonString = "";
        try {
            response = simurClient.criarPagamento(request);
            detalhesJsonString = new ObjectMapper().writeValueAsString(response.getPaymentDetails());

            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(TipoPagamento.PIX_DYNAMIC);
            pagamento.setValor(response.getAmount());
            pagamento.setSituacao(StatusPagamento.getByDisplayValue(response.getStatus()));
            pagamento.setDataCriacao(Instant.now());
            pagamento.setDataAtualizacao(Instant.now());
            pagamento.setCodigoTransacao(UUID.fromString(response.getId()));
            pagamento.setDetalhes(detalhesJsonString);
            
            return pagamento;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("SIMUR PIX RESPONSE: " + detalhesJsonString);
        }
        return null;
    }

}
