package com.parceiroferramentas.api.parceiro_api.model.pagamento;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parceiroferramentas.api.parceiro_api.enums.ParceiroFerramentasInfo;
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.model.pedido.Pedido;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.SimurPaymentService;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.BoletoDetails;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentRequest;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentResponse;

@Component
public class BoletoStrategy implements PagamentoStrategy {
    private final SimurPaymentService simurClient = new SimurPaymentService();

    @Override
    public Pagamento processar(Pedido pedido, String detalhesPagamento) {
        Comprador usuario = pedido.getComprador();
        String dataPedido = DateTimeFormatter.ofPattern("yyyyMMddhhmm").format(LocalDateTime.now(ZoneOffset.systemDefault()));
        
        SimurPaymentRequest request = new SimurPaymentRequest(
            "PF-" + dataPedido + "-" + pedido.getId(),             // external_order_id
            pedido.getValorTotal(),
            "BRL",
            ParceiroFerramentasInfo.LOJA_DOCUMENTO.getString(),
            pedido.getComprador().getDocumento(),
            new BoletoDetails(
                ParceiroFerramentasInfo.BANCO.getString(),
                ParceiroFerramentasInfo.AGENCIA.getString(),
                ParceiroFerramentasInfo.CONTA.getString(),
                "101",
                ParceiroFerramentasInfo.LOJA_DOCUMENTO.getString(),
                ParceiroFerramentasInfo.LOJA_NOME.getString(),
                usuario.getNome()
            )
        );

        SimurPaymentResponse response;
        try {
            response = simurClient.criarPagamento(request);
            String detalhesJsonString = new ObjectMapper().writeValueAsString(response.getPaymentDetails());

            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(TipoPagamento.BOLETO);
            pagamento.setValor(response.getAmount());
            pagamento.setSituacao(StatusPagamento.getByDisplayValue(response.getStatus()));
            pagamento.setDataCriacao(Instant.now());
            pagamento.setDataAtualizacao(Instant.now());
            pagamento.setCodigoTransacao(UUID.fromString(response.getId()));
            pagamento.setDetalhes(detalhesJsonString);
            return pagamento;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
