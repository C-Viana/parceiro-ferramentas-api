package com.parceiroferramentas.api.parceiro_api.exception;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleAllExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDate.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalApplicationException.class)
    public final ResponseEntity<ExceptionResponseTemplate> InternalApplicationExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDate.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalApplicationException.class)
    public final ResponseEntity<ExceptionResponseTemplate> handleBadRequestExceptions(Exception error, WebRequest request) {
        ExceptionResponseTemplate response = new ExceptionResponseTemplate(LocalDate.now(), error.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
