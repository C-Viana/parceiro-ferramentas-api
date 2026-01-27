package com.parceiroferramentas.api.parceiro_api.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleAllExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDateTime.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> erros = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
            .collect(Collectors.toList());
        
        String mensagem = String.join("; ", erros);
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDateTime.now(), mensagem, request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleBadRequestExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDateTime.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAuthorizationException.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleInvalidAuthorizationExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDateTime.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleUsernameNotFoundExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDateTime.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleNotFoundExceptionExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDateTime.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
