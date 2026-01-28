package com.parceiroferramentas.api.parceiro_api.health;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.enums.PERFIL_ACESSO;

@Component
public class JwtHealthIndicator implements HealthIndicator {

    private final JwtTokenService jwtService;

    public JwtHealthIndicator(JwtTokenService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public @Nullable Health health() {
        try {
            AcessoUsuarioDto acesso = jwtService.gerarAcesso("dummy-user", List.of(PERFIL_ACESSO.CLIENTE));
            jwtService.decodeToken(acesso.acesso());
            return Health.up().withDetail("jwt", "Validação de token OK").build();
        } catch (Exception e) {
            return Health.down().withDetail("jwt", "Falha na validação de token").withException(e).build();
        }
    }

}
