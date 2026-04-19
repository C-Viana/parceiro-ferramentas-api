package com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.BoletoDetails;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.CreditCardDetails;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.DebitCardDetails;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.PixDynamicResponseDetails;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BoletoDetails.class, name = "BOLETO"),
    @JsonSubTypes.Type(value = PixDynamicResponseDetails.class, name = "PIX_DYNAMIC"),
    @JsonSubTypes.Type(value = CreditCardDetails.class, name = "CREDIT_CARD"),
    @JsonSubTypes.Type(value = DebitCardDetails.class, name = "DEBIT_CARD"),
})
public interface PaymentDetailsInterface {}
