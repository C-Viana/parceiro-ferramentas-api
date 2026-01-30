package com.parceiroferramentas.api.parceiro_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.parceiroferramentas.api.parceiro_api.auth.JwtFilter;
import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.enums.PERFIL_ACESSO;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenService provider;
    private static AuthenticationManager authManager;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        authManager = config.getAuthenticationManager();
        return authManager;
    }

    public Authentication authenticate(String username, String password) {
        return authManager.authenticate( new UsernamePasswordAuthenticationToken(username, password) );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        JwtFilter filter = new JwtFilter(provider);
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable) // Recomendável habilitar em produção se houver integração com frontend
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                reqs -> reqs.requestMatchers(
                    "/usuarios/signin", 
                    "/usuarios/signup", 
                    "/usuarios/refresh/**", 
                    "/swagger-ui/**", 
                    "/swagger-ui.html", 
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/ferramentas").hasAnyAuthority(PERFIL_ACESSO.ADMIN.getString())
                .requestMatchers(HttpMethod.PUT, "/api/v1/ferramentas").hasAnyAuthority(PERFIL_ACESSO.ADMIN.getString(), PERFIL_ACESSO.GERENTE.getString())
                .requestMatchers(HttpMethod.GET, "/usuarios", "/usuarios/**", "/api/v1/endereco/todos", "/api/v1/pedido/{pedidoId}").hasAnyAuthority(PERFIL_ACESSO.ADMIN.getString(), PERFIL_ACESSO.GERENTE.getString())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/ferramentas").hasAnyAuthority(PERFIL_ACESSO.ADMIN.getString())
                .requestMatchers(HttpMethod.GET, "/api/v1/ferramentas", "/api/v1/ferramentas/**").permitAll()
                .requestMatchers(
                    "/api/v1/gerenciamento",
                    "/api/v1/gerenciamento/**",
                    "/api/v1/endereco",
                    "/api/v1/endereco/**", 
                    "/api/v1/carrinho",
                    "/api/v1/carrinho/**",
                    "/api/v1/pedido",
                    "/api/v1/pedido/**"
                ).authenticated()
            )
            .cors(cors -> cors.configurationSource( request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("https://parceiro-ferramentas-api-production.up.railway.app"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                })
            )
            .build();
    }

}
