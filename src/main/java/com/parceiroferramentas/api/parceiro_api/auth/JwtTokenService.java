package com.parceiroferramentas.api.parceiro_api.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.enums.PERFIL_ACESSO;
import com.parceiroferramentas.api.parceiro_api.exception.InvalidAuthorizationException;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenService {

    @Value("${api.security.token.secret}") //Utilizar esse parâmetro nas propriedades do projeto para externalizar a chave de descriptografia.
    private String secretKey;
    private final long DURATION = 3;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private UserDetailsService service;

    Algorithm algorithm = null;

    public JwtTokenService(@Lazy UserDetailsService service) {
        this.service = service;
    }

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public AcessoUsuarioDto gerarAcesso(String username, List<PERFIL_ACESSO> permissions) {
        List<String> listaPermissoes = permissions.stream().map(PERFIL_ACESSO::getString).toList();
        startTime = LocalDateTime.now();
        endTime = startTime.plus(DURATION, ChronoUnit.HOURS);

        String accessToken = getTokenAcesso(username, listaPermissoes, startTime, endTime);
        String refreshToken = getTokenRenovacao(username, listaPermissoes, startTime);

        return new AcessoUsuarioDto(
            username,
            true,
            startTime,
            endTime,
            accessToken,
            refreshToken
        );
    }

    public AcessoUsuarioDto renovarAcesso(String refreshToken) {
        if(refreshToken.contains("Bearer"))
            refreshToken = refreshToken.substring("Bearer ".length());

        DecodedJWT decodedJWT = decodeToken(refreshToken);
        return gerarAcesso(decodedJWT.getSubject(), decodedJWT.getClaim("roles").asList(PERFIL_ACESSO.class));
    }

    private String getTokenAcesso(String username, List<String> permissions, LocalDateTime startTime, LocalDateTime endTime) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        
        ZonedDateTime zonedDateTime = startTime.atZone(ZoneId.systemDefault());
        Instant startTimeInstant = zonedDateTime.toInstant();

        zonedDateTime = endTime.atZone(ZoneId.systemDefault());
        Instant endTimeInstant = zonedDateTime.toInstant();
        
        return JWT.create()
            .withIssuer(issuerUrl)
            .withSubject(username)
            .withClaim("roles", permissions)
            .withIssuedAt(startTimeInstant)
            .withExpiresAt(endTimeInstant)
            .sign(algorithm);
    }

    private String getTokenRenovacao(String username, List<String> permissions, LocalDateTime startTime) {
        ZonedDateTime zonedDateTime = startTime.atZone(ZoneId.systemDefault());
        Instant startTimeInstant = zonedDateTime.toInstant();

        LocalDateTime renewal = startTime.plus((DURATION*3), ChronoUnit.HOURS);
        zonedDateTime = renewal.atZone(ZoneId.systemDefault());
        Instant renewalInstant = zonedDateTime.toInstant();
        
        return JWT.create()
            .withClaim("roles", permissions)
            .withIssuedAt(startTimeInstant)
            .withExpiresAt(renewalInstant)
            .withSubject(username)
            .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new InvalidAuthorizationException("O TOKEN INFORMADO EXPIROU OU CONTEM ERROS");
        }
        
        return verifier.verify(token);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        UserDetails user = service.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String extrairTokenDoHeader(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken == null || StringUtils.isBlank(bearerToken) || (!bearerToken.startsWith("Bearer"))) {
            return null;
        }
        return bearerToken.split(" ")[1];
    }

    public boolean tokenExpirado(String token) {
        if(decodeToken(token).getExpiresAt().before(new Date()))
            return true;
        return false;
    }

    public String getUserFromToken(String token) {
        try {
            return decodeToken(token).getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }
}
