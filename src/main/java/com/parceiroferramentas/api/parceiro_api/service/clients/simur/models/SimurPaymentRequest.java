package com.parceiroferramentas.api.parceiro_api.service.clients.simur.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato.PaymentDetailsInterface;

public record SimurPaymentRequest(
    @JsonProperty(value = "external_order_id")
    String externalOrderId,

    @JsonProperty(value = "amount")
    BigDecimal amount,

    @JsonProperty(value = "currency")
    String currency,

    @JsonProperty(value = "seller_document")
    String sellerDocument,

    @JsonProperty(value = "payer_document")
    String payerDocument,

    @JsonProperty(value = "payment_details")
    PaymentDetailsInterface details
) {}
