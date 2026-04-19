package com.parceiroferramentas.api.parceiro_api.service.clients.simur.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato.PaymentDetailsInterface;

@JsonTypeName(value = "PIX_DYNAMIC")
public class PixDynamicRequestDetails implements PaymentDetailsInterface {
    @JsonProperty(value = "description")
    String description;

    public PixDynamicRequestDetails() {}

    public PixDynamicRequestDetails(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
