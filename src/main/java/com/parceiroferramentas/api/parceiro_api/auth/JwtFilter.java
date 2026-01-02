package com.parceiroferramentas.api.parceiro_api.auth;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenService service;

    public JwtFilter(JwtTokenService provider) {
        this.service = provider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filters) throws IOException, ServletException {
        String token = service.extrairTokenDoHeader((HttpServletRequest)req);

        if(token != null && !token.isBlank()) {
            if(!service.tokenExpirado(token)) {
                Authentication auth = service.getAuthentication(token);
                if(auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filters.doFilter(req, res);
    }

}
