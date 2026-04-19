package com.parceiroferramentas.api.parceiro_api.service.clients.simur.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato.PaymentDetailsInterface;

public class DebitCardDetails implements PaymentDetailsInterface {
    @JsonProperty(value = "card_token")
    String cardToken;

    public DebitCardDetails() {
    }

    public DebitCardDetails(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cardToken == null) ? 0 : cardToken.hashCode());
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
        CreditCardDetails other = (CreditCardDetails) obj;
        if (cardToken == null) {
            if (other.cardToken != null)
                return false;
        } else if (!cardToken.equals(other.cardToken))
            return false;
        return true;
    }
}
