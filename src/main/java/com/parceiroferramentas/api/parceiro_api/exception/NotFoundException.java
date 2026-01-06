package com.parceiroferramentas.api.parceiro_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotFoundException extends RuntimeException {
    
    public NotFoundException() {
        super("Nenhum registro encontrado");
    }

	public NotFoundException(String message) {
        super(message);
    }
}
