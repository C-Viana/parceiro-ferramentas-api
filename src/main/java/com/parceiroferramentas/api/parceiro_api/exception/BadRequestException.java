package com.parceiroferramentas.api.parceiro_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    
    public BadRequestException() {
        super("A requisição conta com dados inválidos ou campos obrigatórios ausentes");
    }

	public BadRequestException(String message) {
        super(message);
    }
}
