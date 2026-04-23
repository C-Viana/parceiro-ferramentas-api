package com.parceiroferramentas.api.parceiro_api.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.parceiroferramentas.api.parceiro_api.service.clients.simur.models.SimurPaymentResponse;

public class CreateMockedSimurPaymentData {
    private File jsonPayments = new File("src/test/resources/SimurPaymentResponses.json");

    public static CreateMockedSimurPaymentData getInstance() {
        return new CreateMockedSimurPaymentData();
    }

    public List<SimurPaymentResponse> getPayments() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        
        mapper.registerModule(module);

        try {
            return Arrays.asList(mapper.readValue(jsonPayments, SimurPaymentResponse[].class));
        } catch (StreamReadException e) {
            throw new RuntimeException("Erro na leitura do arquivo Json", e);
        } catch (DatabindException e) {
            throw new RuntimeException("Erro ao mapear o Json para o objeto Ferramenta", e);
        } catch (IOException e) {
            throw new RuntimeException("Erro de I/O ao processar o arquivo Json", e);
        }
    }

    public String getPaymentAsJsonString(int index) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ArrayNode jsonArray = (ArrayNode)mapper.readTree(jsonPayments);
            return jsonArray.get(index).toPrettyString();
        } catch (StreamReadException e) {
            throw new RuntimeException("Erro na leitura do arquivo Json", e);
        }  catch (IOException e) {
            throw new RuntimeException("Erro de I/O ao processar o arquivo Json", e);
        }
    }
}
