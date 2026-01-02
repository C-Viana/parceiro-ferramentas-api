package com.parceiroferramentas.api.parceiro_api.controller.openapi;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.parceiroferramentas.api.parceiro_api.dto.AcessoUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.CredenciaisUsuarioDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.UsuarioResponseDto;
import com.parceiroferramentas.api.parceiro_api.exception.ExceptionResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = "Usuários", description = "Endpoints para cadastro e login de usuários")
public interface UsuarioDocumentation {

    @Operation(
        summary = "Login",
        description = "Realiza a autenticação de um usuários atráves de um nome de usuário e senha",
        tags = "Autenticação",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = AcessoUsuarioDto.class)
                    )
                }
            ),
            @ApiResponse(
                description = "Bad Request", 
                responseCode = "400",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Not Found", 
                responseCode = "404",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Internal Error", 
                responseCode = "500",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<?> entrar(CredenciaisUsuarioDto credenciais);

    @Operation(
        summary = "Renovação",
        description = "Renova e estende o acesso através de um \"refresh token\"",
        tags = "Autenticação",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = AcessoUsuarioDto.class)
                    )
                }
            ),
            @ApiResponse(
                description = "Bad Request", 
                responseCode = "400",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Internal Error", 
                responseCode = "500",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<?> renovar(String nomeUsuario, String refreshToken);

    @Operation(
        summary = "Cadastro",
        description = "Cria um novo usuário",
        tags = "Autenticação",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "201",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UsuarioResponseDto.class)
                    )
                }
            ),
            @ApiResponse(
                description = "Bad Request", 
                responseCode = "400",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Internal Error", 
                responseCode = "500",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<?> cadastrarUsuario(UsuarioRequestDto usuario);

    @Operation(
        summary = "Busca todos os usuários",
        description = "Retorna uma lista de todos os usuários cadastrados",
        tags = "Usuários",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "201",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))
                    )
                }
            ),
            @ApiResponse(
                description = "Bad Request", 
                responseCode = "400",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Internal Error", 
                responseCode = "500",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Page<UsuarioResponseDto>> findAllUsuarios(
        @RequestHeader("Authorization") String token,
        @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) int page, 
        @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24)int size);

     @Operation(
        summary = "Busca todos os usuários por perfil",
        description = "Retorna uma lista de todos os usuários cadastrados que tenham o perfil específico",
        tags = "Usuários",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "201",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))
                    )
                }
            ),
            @ApiResponse(
                description = "Bad Request", 
                responseCode = "400",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Internal Error", 
                responseCode = "500",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Page<UsuarioResponseDto>> findUsuariosPorPerfil(
        @RequestHeader("Authorization") String token,
        @PathVariable String perfil, 
        @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) int page, 
        @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24) int size);
}
