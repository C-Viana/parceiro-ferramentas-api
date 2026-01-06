package com.parceiroferramentas.api.parceiro_api.controller.openapi;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.parceiroferramentas.api.parceiro_api.dto.EnderecoDto;
import com.parceiroferramentas.api.parceiro_api.exception.ExceptionResponseTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Endereços", description = "CRUD para tratativa do recurso Endereços")
public interface EnderecoDocumentation {

    @Operation(
        summary = "Buscar todos os endereços",
        description = "Recupera todos os endereços cadastrados",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = EnderecoDto.class))
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
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Unauthorized", 
                responseCode = "403",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Page<EnderecoDto>> getEnderecos( 
            @RequestParam(value = "indice", defaultValue = "0") Integer page, 
            @RequestParam(value = "quant", defaultValue = "12") Integer size, 
            @RequestParam(value = "ordem", defaultValue = "asc") String sort
        );

    @Operation(
        summary = "Buscar endereço",
        description = "Recupera um endereço pelo seu ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = EnderecoDto.class)
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
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Unauthorized", 
                responseCode = "403",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<EnderecoDto> getEnderecoById(@PathVariable Long id);

    @Operation(
        summary = "Buscar endereços de um usuário",
        description = "Recupera todos os endereços pelo ID do usuário",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = EnderecoDto.class))
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
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Unauthorized", 
                responseCode = "403",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<List<EnderecoDto>> findEnderecosDoUsuario(@PathVariable Long id);

    @Operation(
        summary = "Cadastra um novo endereço",
        description = "Cria um novo registro de endereço vinculado ao ID de usuário",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Success",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = EnderecoDto.class)
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
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Unauthorized", 
                responseCode = "403",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<EnderecoDto> cadastrarEndereco(@PathVariable Long usuarioId, @RequestBody EnderecoDto dto);

    @Operation(
        summary = "Atualiza um endereço",
        description = "Altera dados de um registro de endereço",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = EnderecoDto.class)
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
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Unauthorized", 
                responseCode = "403",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<EnderecoDto> atualizarEndereco(@PathVariable Long id, @RequestBody EnderecoDto dto);

    @Operation(
        summary = "Apaga um endereço",
        description = "Apaga definitivamente um registro de endereço",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Success",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = EnderecoDto.class)
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
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class))),
            @ApiResponse(
                description = "Unauthorized", 
                responseCode = "403",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<?> removerEndereco(@PathVariable Long id);

}
