package com.parceiroferramentas.api.parceiro_api.exception;

import java.time.LocalDateTime;

public record ExceptionResponseTemplate(LocalDateTime timestamp, String message, String details) {

}
