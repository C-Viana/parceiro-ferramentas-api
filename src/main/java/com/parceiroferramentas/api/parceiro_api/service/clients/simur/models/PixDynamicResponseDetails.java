package com.parceiroferramentas.api.parceiro_api.service.clients.simur.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato.PaymentDetailsInterface;
import com.parceiroferramentas.api.parceiro_api.serializers.LocalDateTimeDeserializer;

public class PixDynamicResponseDetails implements PaymentDetailsInterface {
    @JsonProperty(value = "qr_code_base64")
    String qrCodeBase64;
    
    @JsonProperty(value = "payment_uri")
    String paymentUri;
    
    @JsonProperty(value = "txid")
    String txid;
    
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty(value = "expires_at")
    LocalDateTime expiresAt;
    
    @JsonProperty(value = "description")
    String description;

    public PixDynamicResponseDetails() {}

    public PixDynamicResponseDetails(String description) {
        this.description = description;
    }

    public PixDynamicResponseDetails(String qrCodeBase64, String paymentUri, String txid, LocalDateTime expiresAt,
            String description) {
        this.qrCodeBase64 = qrCodeBase64;
        this.paymentUri = paymentUri;
        this.txid = txid;
        this.expiresAt = expiresAt;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public String getPaymentUri() {
        return paymentUri;
    }

    public void setPaymentUri(String paymentUri) {
        this.paymentUri = paymentUri;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((qrCodeBase64 == null) ? 0 : qrCodeBase64.hashCode());
        result = prime * result + ((paymentUri == null) ? 0 : paymentUri.hashCode());
        result = prime * result + ((txid == null) ? 0 : txid.hashCode());
        result = prime * result + ((expiresAt == null) ? 0 : expiresAt.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
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
        PixDynamicResponseDetails other = (PixDynamicResponseDetails) obj;
        if (qrCodeBase64 == null) {
            if (other.qrCodeBase64 != null)
                return false;
        } else if (!qrCodeBase64.equals(other.qrCodeBase64))
            return false;
        if (paymentUri == null) {
            if (other.paymentUri != null)
                return false;
        } else if (!paymentUri.equals(other.paymentUri))
            return false;
        if (txid == null) {
            if (other.txid != null)
                return false;
        } else if (!txid.equals(other.txid))
            return false;
        if (expiresAt == null) {
            if (other.expiresAt != null)
                return false;
        } else if (!expiresAt.equals(other.expiresAt))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        return true;
    }

}
