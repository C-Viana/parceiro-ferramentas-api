package com.parceiroferramentas.api.parceiro_api.service.clients.simur.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parceiroferramentas.api.parceiro_api.model.pagamento.contrato.PaymentDetailsInterface;

@JsonIgnoreProperties(
    value = {"generated_at", "due_date", "our_number", "expiration_days", "instructions"}
)
public class BoletoDetails implements PaymentDetailsInterface {
    @JsonProperty(value = "bank_code")
    String bankCode;

    @JsonProperty(value = "agency")
    String agencyCode;

    @JsonProperty(value = "account")
    String bankAccount;

    @JsonProperty(value = "document_modality")
    String docModality;

    @JsonProperty(value = "beneficiary_id")
    String merchantDoc;

    @JsonProperty(value = "beneficiary_name")
    String merchantName;

    @JsonProperty(value = "payer_name")
    String payerName;

    @JsonProperty(value = "barcode")
    String barcode;

    @JsonProperty(value = "digitable_line")
    String digitableLine;

    @JsonProperty(value = "boleto_url")
    String boletoUrl;

    public BoletoDetails(){}
    
    public BoletoDetails(String bankCode, String agencyCode, String bankAccount, String docModality,
            String merchantDoc, String merchantName, String payerName) {
        this.bankCode = bankCode;
        this.agencyCode = agencyCode;
        this.bankAccount = bankAccount;
        this.docModality = docModality;
        this.merchantDoc = merchantDoc;
        this.merchantName = merchantName;
        this.payerName = payerName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDocModality() {
        return docModality;
    }

    public void setDocModality(String docModality) {
        this.docModality = docModality;
    }

    public String getMerchantDoc() {
        return merchantDoc;
    }

    public void setMerchantDoc(String merchantDoc) {
        this.merchantDoc = merchantDoc;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDigitableLine() {
        return digitableLine;
    }

    public void setDigitableLine(String digitableLine) {
        this.digitableLine = digitableLine;
    }

    public String getBoletoUrl() {
        return boletoUrl;
    }

    public void setBoletoUrl(String boletoUrl) {
        this.boletoUrl = boletoUrl;
    }

}
