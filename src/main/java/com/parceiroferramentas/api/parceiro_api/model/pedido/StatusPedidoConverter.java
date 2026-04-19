package com.parceiroferramentas.api.parceiro_api.model.pedido;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusPedidoConverter implements AttributeConverter<StatusPedido, String> {

    @Override
    public String convertToDatabaseColumn(StatusPedido attribute) {
        return attribute == null ? null : attribute.getString();
    }

    @Override
    public StatusPedido convertToEntityAttribute(String dbData) {
        return dbData == null ? null : StatusPedido.getByDisplayValue(dbData);
    }

}
