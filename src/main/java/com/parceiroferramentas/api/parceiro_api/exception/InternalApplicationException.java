package com.parceiroferramentas.api.parceiro_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalApplicationException extends RuntimeException {
    
    public InternalApplicationException() {
        super("A aplicação teve um erro e não pode concluir o processamento. Tente novamente mais tarde");
    }

	public InternalApplicationException(String message) {
        super(message);
    }
}
