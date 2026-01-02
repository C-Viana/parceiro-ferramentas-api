package com.parceiroferramentas.api.parceiro_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class InvalidAuthorizationException extends RuntimeException {
    
    public InvalidAuthorizationException() {
        super("Permissão de acesso negada. Verifique se a autenticação foi realizada corretamente");
    }

	public InvalidAuthorizationException(String message) {
        super(message);
    }
}
