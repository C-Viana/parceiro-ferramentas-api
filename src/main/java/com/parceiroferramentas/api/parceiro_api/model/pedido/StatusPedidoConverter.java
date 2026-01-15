package com.parceiroferramentas.api.parceiro_api.model.pedido;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusPedidoConverter implements AttributeConverter<STATUS_PEDIDO, String> {

    @Override
    public String convertToDatabaseColumn(STATUS_PEDIDO attribute) {
        return attribute == null ? null : attribute.getString();
    }

    @Override
    public STATUS_PEDIDO convertToEntityAttribute(String dbData) {
        return dbData == null ? null : STATUS_PEDIDO.getByDisplayValue(dbData);
    }

}
