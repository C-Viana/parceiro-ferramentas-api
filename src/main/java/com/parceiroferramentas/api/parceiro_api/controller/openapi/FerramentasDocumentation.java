package com.parceiroferramentas.api.parceiro_api.controller.openapi;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.parceiroferramentas.api.parceiro_api.dto.FerramentaDto;
import com.parceiroferramentas.api.parceiro_api.exception.ExceptionResponseTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Tag(name = "Ferramentas", description = "CRUD para tratativa do recurso Ferramentas")
public interface FerramentasDocumentation {

    @Operation(
        summary = "Buscar por ID",
        description = "Localiza uma ferramenta cadastrada através de seu identificador único",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FerramentaDto.class)
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<FerramentaDto> findById(@PathVariable @Min(0) Long id);

    @Operation(
        summary = "Cadastrar nova ferramenta",
        description = "Insere no banco de dados uma nova ferramenta com todas as suas informações pertinentes",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "201",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FerramentaDto.class)
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<FerramentaDto> create(@RequestHeader("Authorization") String token, @RequestBody FerramentaDto ferramenta);

    @Operation(
        summary = "Remove uma ferramenta por ID",
        description = "Localiza uma ferramenta pelo seu identificador único e a remove da base de dados",
        responses = {
            @ApiResponse(
                description = "No Content", 
                responseCode = "204",
                content = @Content),
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(
        summary = "Atualiza dados de uma ferramenta",
        description = "Localiza uma ferramenta cadastrada através de seu identificador único e sobrescreve os campos alterados",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FerramentaDto.class)
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<FerramentaDto> update(@PathVariable Long id, @RequestBody FerramentaDto ferramenta);

     @Operation(
        summary = "Lista todas as ferramentas",
        description = "Retorna uma lista paginada de todas as ferramentas do banco de dados",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = FerramentaDto.class))
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Page<FerramentaDto>> findAll(
        @RequestParam(value = "indice", defaultValue = "0") Integer page, 
        @RequestParam(value = "quant", defaultValue = "12") Integer size, 
        @RequestParam(value = "ordem", defaultValue = "asc") String sort);

    @Operation(
        summary = "Lista todas as ferramentas de um dado tipo",
        description = "Retorna uma lista paginada de todas as ferramentas do banco de dados que tiverem o tipo informado",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = FerramentaDto.class))
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Page<FerramentaDto>> findAllByType(
        @RequestParam String tipo, 
        @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) Integer page, 
        @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24) Integer size, 
        @RequestParam(value = "ordem", defaultValue = "asc") @Pattern(regexp="asc|desc") String sort
    );

    @Operation(
        summary = "Lista todas as ferramentas pelo nome",
        description = "Retorna uma lista paginada de todas as ferramentas do banco de dados que contém no nome a informação enviada",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = FerramentaDto.class))
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
                responseCode = "401",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponseTemplate.class)))
        }
    )
    ResponseEntity<Page<FerramentaDto>> findAllByNome(
        @RequestParam String nome, 
        @RequestParam(value = "indice", defaultValue = "0") @Min(0) @Max(199) Integer page, 
        @RequestParam(value = "quant", defaultValue = "12") @Min(1) @Max(24) Integer size, 
        @RequestParam(value = "ordem", defaultValue = "asc") @Pattern(regexp="asc|desc") String sort
    );
}
