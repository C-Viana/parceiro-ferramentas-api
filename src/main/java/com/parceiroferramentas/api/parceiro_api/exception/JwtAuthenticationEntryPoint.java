package com.parceiroferramentas.api.parceiro_api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
            throws IOException, ServletException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        String responseMessage = (authException.getMessage() == null || authException.getMessage().isBlank()) 
            ? "O TOKEN INFORMADO EXPIROU OU CONTEM ERROS"
            : authException.getMessage();

        ExceptionResponseTemplate errorResponse = new ExceptionResponseTemplate(
                LocalDateTime.now(),
                responseMessage,
                request.getServletPath()
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}