package com.parceiroferramentas.api.parceiro_api.exception;

import java.time.LocalDate;

public record ExceptionResponseTemplate(LocalDate timestamp, String message, String details) {

}
