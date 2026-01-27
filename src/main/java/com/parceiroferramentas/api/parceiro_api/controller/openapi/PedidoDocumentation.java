package com.parceiroferramentas.api.parceiro_api.controller.openapi;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.parceiroferramentas.api.parceiro_api.dto.PagamentoRequestDto;
import com.parceiroferramentas.api.parceiro_api.dto.PedidoResponseDto;
import com.parceiroferramentas.api.parceiro_api.exception.ExceptionResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Tag(name = "Pedido", description = "Recursos para recuperação, criação e atualização de pedidos")
public interface PedidoDocumentation {

    @Operation(
        summary = "Criar pedido de compra",
        description = "Realiza um pedido de compra a partir do carrinho do usuário",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "created",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PedidoResponseDto.class)
                )
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
    ResponseEntity<PedidoResponseDto> criarPedidoDeCompra(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @PathVariable Long enderecoId, 
        @Valid
        @RequestBody PagamentoRequestDto pagamento) throws JsonProcessingException;

    @Operation(
        summary = "Criar pedido de aluguel",
        description = "Realiza um pedido de aluguel a partir do carrinho do usuário",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "created",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PedidoResponseDto.class)
                )
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
    ResponseEntity<PedidoResponseDto> criarPedidoDeAluguel(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token, 
        @PathVariable Long diasPrazo, 
        @PathVariable Long enderecoId, 
        @Valid
        @RequestBody PagamentoRequestDto pagamento) throws JsonProcessingException;

    @Operation(
        summary = "Retorna um pedido",
        description = "Retorna um pedido pelo seu ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PedidoResponseDto.class)
                )
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
    ResponseEntity<PedidoResponseDto> buscarPedidoPorId(
        @PathVariable Long pedidoId);

    @Operation(
        summary = "Retorna os pedidos do usuário",
        description = "Retorna lista de pedidos vinculados a um usuário",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDto.class))
                )
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
    ResponseEntity<List<PedidoResponseDto>> buscarPedidosDoUsuario(
        @Valid
        @NotNull(message = "O token de acesso não pode ser nulo")
        @NotBlank(message = "O token de acesso está vazio")
        @RequestHeader("Authorization") String token
    );

    @Operation(
        summary = "Atualiza data de finalização",
        description = "Altera a data de finalização do pedido recebendo texto com formato dd-MM-yyyy",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PedidoResponseDto.class)
                )
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
    ResponseEntity<PedidoResponseDto> atualizarDataFimDoPedido(
        @Valid
        @NotNull(message = "O parâmetro de identificação do pedido está nulo")
        @Min(value = 1, message = "O identificador do pedido não pode ser inferior a 1")
        @RequestParam(name = "pedido_id", required = true) Long pedido_id,
        @Valid
        @NotNull(message = "O parâmetro data (dd/mm/yyyy) não foi informado")
        @NotBlank(message = "O parâmetro data (dd/mm/yyyy) foi enviado vazio")
        @Pattern(regexp = "[0-9]{2}-[0-9]{2}-[0-9]{4}", message = "O formato da data deve seguir o padrão dd-MM-yyyy")
        @RequestParam(name = "nova_data", required = true) String nova_data
    );

    @Operation(
        summary = "Atualiza situação do pedido",
        description = "Altera a situação do pedido recebendo texto com valor a ser aplicado",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PedidoResponseDto.class)
                )
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
    ResponseEntity<PedidoResponseDto> atualizarSituacaoDoPedido(
        @Valid
        @NotNull(message = "O parâmetro de identificação do pedido está nulo")
        @RequestParam(name = "pedido_id", required = true) Long pedido_id,
        @Valid
        @NotNull(message = "O parâmetro situação não foi informado")
        @NotBlank(message = "O parâmetro situação foi enviado vazio")
        @RequestParam(name = "nova_situacao", required = true) String nova_situacao
    );
}
