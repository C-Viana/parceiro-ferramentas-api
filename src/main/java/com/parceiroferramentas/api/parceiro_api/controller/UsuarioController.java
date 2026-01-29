package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parceiroferramentas.api.parceiro_api.controller.openapi.UsuarioDocumentation;
import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioResponseDto;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.service.UsuarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/usuarios")
public class UsuarioController implements UsuarioDocumentation {

    private final UsuarioService service;
    private final GlobalObjectMapper mapper;

    @Override
    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> entrar(@Valid @RequestBody CredenciaisUsuarioDto credenciais) {
        if(credenciais == null || credenciais.senha().isBlank() || credenciais.username().isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados de acesso precisam ser informados");
        AcessoUsuarioDto token = service.signin(credenciais);
        return ResponseEntity.ok().body(token);
    }

    @Override
    @PutMapping(value = "/refresh/{nomeUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> renovar(
        @Valid
        @NotBlank(message = "O nome de usuário não pode estar vazio")
        @NotNull(message = "O nome de usuário não pode ser nulo")
        @PathVariable String nomeUsuario, 
        @Valid
        @NotBlank(message = "O token não pode estar vazio")
        @NotNull(message = "O token não pode ser nulo")
        @RequestHeader("Authorization") String refreshToken
    ) {
        AcessoUsuarioDto token = service.refresh(nomeUsuario, refreshToken);
        return ResponseEntity.ok().body(token);
    }

    @Override
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDto usuario) {
        log.info("REALIZANDO CADASTRO DE NOVO USUÁRIO");
        Usuario res = service.signup(mapper.toUsuarioEntity(usuario));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(res.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(mapper.toUsuarioResponseDto(res));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UsuarioResponseDto>> findAllUsuarios(
            @Valid
            @NotBlank(message = "O token não pode estar vazio")
            @NotNull(message = "O token não pode ser nulo")
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) int page, 
            @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24)int size
        ) {
        log.info("REALIZANDO BUSCA DE LISTA DE USUÁRIOS");
        Page<Usuario> response = service.findAllUsuarios(token.substring("Bearer ".length()), PageRequest.of(page, size, Sort.by(Direction.ASC, "id")));
        Page<UsuarioResponseDto> dtoPage = response.map(user -> mapper.toUsuarioResponseDto(user));

        return ResponseEntity.ok(dtoPage);
    }

    @Override
    @GetMapping(value = "/perfil/{perfil}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UsuarioResponseDto>> findUsuariosPorPerfil(
            @Valid
            @NotBlank(message = "O token não pode estar vazio")
            @NotNull(message = "O token não pode ser nulo")
            @RequestHeader("Authorization") String token,
            @PathVariable String perfil, 
            @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) int page, 
            @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24) int size
        ) {
        log.info("REALIZANDO BUSCA DE USUÁRIOS COM O PERFIL: {}", perfil);
        Page<Usuario> response = service.findByAuthoritiesContains(token.substring("Bearer ".length()), perfil, PageRequest.of(page, size, Sort.by(Direction.ASC, "id")));
        Page<UsuarioResponseDto> dtoPage = response.map(user -> mapper.toUsuarioResponseDto(user));

        return ResponseEntity.ok(dtoPage);
    }

}
