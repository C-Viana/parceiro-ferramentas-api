package com.parceiroferramentas.api.parceiro_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.parceiroferramentas.api.parceiro_api.auth.JwtFilter;
import com.parceiroferramentas.api.parceiro_api.auth.JwtTokenService;
import com.parceiroferramentas.api.parceiro_api.enums.PerfisAcesso;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenService provider;

    public SecurityConfig(JwtTokenService provider) {
        this.provider = provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        JwtFilter filter = new JwtFilter(provider);
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable) // Recomendável habilitar em produção
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                reqs -> reqs.requestMatchers(
                    "/usuarios/signin", "/usuarios/signup", "/usuarios/refresh/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/ferramentas").hasAnyAuthority(PerfisAcesso.ADMIN.getString())
                .requestMatchers(HttpMethod.PUT, "/api/v1/ferramentas").hasAnyAuthority(PerfisAcesso.ADMIN.getString(), PerfisAcesso.GERENTE.getString())
                .requestMatchers(HttpMethod.GET, "/usuarios", "/usuarios/**").hasAnyAuthority(PerfisAcesso.ADMIN.getString(), PerfisAcesso.GERENTE.getString())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/ferramentas").hasAnyAuthority(PerfisAcesso.ADMIN.getString())
                .requestMatchers(HttpMethod.GET, "/api/v1/ferramentas", "/api/v1/ferramentas/**").permitAll()
                .requestMatchers(
                    "/api/v1/gerenciamento",
                    "/api/v1/gerenciamento/**"
                ).authenticated()
            )
            .cors(cors -> {})
            .build();
    }

}
