package com.parceiroferramentas.api.parceiro_api.service.clients.simur.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato.PaymentDetailsInterface;
import com.parceiroferramentas.api.parceiro_api.serializers.DateTimeOffsetDeserializer;

public class SimurPaymentResponse {
    String id;

    @JsonProperty(value = "external_order_id")
    String externalOrderId;
    BigDecimal amount;
    String currency;
    String status;

    @JsonProperty(value = "created_at")
    @JsonDeserialize(using = DateTimeOffsetDeserializer.class)
    LocalDateTime createdAt;

    @JsonProperty(value = "seller_document")
    String sellerDocument;

    @JsonProperty(value = "payer_document")
    String payerDocument;

    @JsonProperty(value = "payment_details")
    PaymentDetailsInterface paymentDetails;

    public SimurPaymentResponse() {}

    public SimurPaymentResponse(String id, String externalOrderId, BigDecimal amount, String currency, String status,
            LocalDateTime createdAt, String sellerDocument, String payerDocument,
            PaymentDetailsInterface details) {
        this.id = id;
        this.externalOrderId = externalOrderId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
        this.sellerDocument = sellerDocument;
        this.payerDocument = payerDocument;
        this.paymentDetails = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSellerDocument() {
        return sellerDocument;
    }

    public void setSellerDocument(String sellerDocument) {
        this.sellerDocument = sellerDocument;
    }

    public String getPayerDocument() {
        return payerDocument;
    }

    public void setPayerDocument(String payerDocument) {
        this.payerDocument = payerDocument;
    }

    public PaymentDetailsInterface getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetailsInterface details) {
        this.paymentDetails = details;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((externalOrderId == null) ? 0 : externalOrderId.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((sellerDocument == null) ? 0 : sellerDocument.hashCode());
        result = prime * result + ((payerDocument == null) ? 0 : payerDocument.hashCode());
        result = prime * result + ((paymentDetails == null) ? 0 : paymentDetails.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimurPaymentResponse other = (SimurPaymentResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (externalOrderId == null) {
            if (other.externalOrderId != null)
                return false;
        } else if (!externalOrderId.equals(other.externalOrderId))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (currency == null) {
            if (other.currency != null)
                return false;
        } else if (!currency.equals(other.currency))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (sellerDocument == null) {
            if (other.sellerDocument != null)
                return false;
        } else if (!sellerDocument.equals(other.sellerDocument))
            return false;
        if (payerDocument == null) {
            if (other.payerDocument != null)
                return false;
        } else if (!payerDocument.equals(other.payerDocument))
            return false;
        if (paymentDetails == null) {
            if (other.paymentDetails != null)
                return false;
        } else if (!paymentDetails.equals(other.paymentDetails))
            return false;
        return true;
    }

}
