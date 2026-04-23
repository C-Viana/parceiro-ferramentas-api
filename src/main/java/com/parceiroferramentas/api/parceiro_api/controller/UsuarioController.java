package com.parceiroferramentas.api.parceiro_api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.parceiroferramentas.api.parceiro_api.dto.CompradorCadastroDto;
import com.parceiroferramentas.api.parceiro_api.dto.CompradorDto;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioResponseDto;
import com.parceiroferramentas.api.parceiro_api.exception.BadRequestException;
import com.parceiroferramentas.api.parceiro_api.mapper.GlobalObjectMapper;
import com.parceiroferramentas.api.parceiro_api.model.Comprador;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;
import com.parceiroferramentas.api.parceiro_api.service.CompradorService;
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

    private final UsuarioService usuarioService;
    private final CompradorService compradorService;
    private final GlobalObjectMapper mapper;

    @Override
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDto usuario) {
        log.info("REALIZANDO CADASTRO DE NOVO USUÁRIO");
        Usuario res = usuarioService.signup(mapper.toUsuarioEntity(usuario));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(res.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(mapper.toUsuarioResponseDto(res));
    }

    @Override
    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> entrar(@Valid @RequestBody CredenciaisUsuarioDto credenciais) {
        if(credenciais == null || credenciais.senha().isBlank() || credenciais.username().isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados de acesso precisam ser informados");
        AcessoUsuarioDto token = usuarioService.signin(credenciais);
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
        AcessoUsuarioDto token = usuarioService.refresh(nomeUsuario, refreshToken);
        return ResponseEntity.ok().body(token);
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
        Page<Usuario> response = usuarioService.findAllUsuarios(token.substring("Bearer ".length()), PageRequest.of(page, size, Sort.by(Direction.ASC, "id")));
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
        Page<Usuario> response = usuarioService.findByAuthoritiesContains(token.substring("Bearer ".length()), perfil, PageRequest.of(page, size, Sort.by(Direction.ASC, "id")));
        Page<UsuarioResponseDto> dtoPage = response.map(user -> mapper.toUsuarioResponseDto(user));

        return ResponseEntity.ok(dtoPage);
    }

    @Override
    @PostMapping(value = "/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarComprador(@Valid @RequestBody CompradorCadastroDto cadastro) {
        log.info("REALIZANDO CADASTRO DE NOVO USUÁRIO");
        UUID requestId = UUID.randomUUID();

        if(compradorService.findByDocumento(cadastro.documento()) != null) throw new BadRequestException("O documento informado já está cadastrado");

        //EFETUANDO CADASTRO DE COMPRADOR COM DADOS DE IDENTIFICAÇÃO, CONTATO E ENDEREÇO
        Comprador request = mapper.toComprador(cadastro);
        request.setId(requestId);
        request.getEnderecos().forEach( item -> item.setComprador(request));
        Comprador resComprador = compradorService.create(request);

        //CRIANDO CREDENCIAIS DE ACESSO PARA O COMPRADOR COM PERFIL CLIENTE
        usuarioService.signup(
            new Usuario(requestId, cadastro.documento(), cadastro.senha(), null)
        );
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resComprador.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(
            mapper.toCompradorDto(resComprador)
        );
    }

    @Override
    @GetMapping(value = "/comprador/documento/{documento}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompradorDto> buscarCompradorPorDocumento(@PathVariable String documento) {
        log.info("BUSCANDO COMPRADOR PELO DOCUMENTO "+documento);
        Comprador res = compradorService.findByDocumento(documento);
        if(res == null) ResponseEntity.notFound();
        return ResponseEntity.ok(mapper.toCompradorDto(res));
    }
    
    @Override
    @PutMapping(value = "/comprador/atualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompradorDto> atualizarComprador(@RequestBody CompradorDto compradorAtualizado) {
        log.info("ATUALIZANDO DADOS DO COMPRADOR DO DOCUMENTO "+compradorAtualizado.documento());
        Comprador original = compradorService.findByDocumento(compradorAtualizado.documento());
        if(original == null) ResponseEntity.notFound();

        Comprador req = mapper.toComprador(compradorAtualizado);
        req.setId(original.getId());
        req.getEnderecos().forEach( item -> item.setComprador(req));
        
        if(compradorService.validarAtualizacaoComprador(original, req))
            ResponseEntity.badRequest().body("Os dados de nome, nascimento, e-mail, telefone e endereço não podem ser nulos nem vazios");

        Comprador response = compradorService.update(req);
        return ResponseEntity.ok(mapper.toCompradorDto(response));
    }

    @Override
    @DeleteMapping(value = "/comprador/documento/{documento}")
    public ResponseEntity<Void> deletarCompradorPorDocumento(@PathVariable String documento) {
        log.info("DELETAR DADOS DO COMPRADOR DO DOCUMENTO "+documento);
        Comprador res = compradorService.findByDocumento(documento);
        if(res == null) return ResponseEntity.noContent().build();
        compradorService.delete(documento);
        usuarioService.deletarUsuario(res.getId());
        return ResponseEntity.noContent().build();
    }

}
