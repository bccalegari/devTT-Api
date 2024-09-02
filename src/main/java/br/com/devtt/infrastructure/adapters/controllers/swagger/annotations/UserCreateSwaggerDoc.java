package br.com.devtt.infrastructure.adapters.controllers.swagger.annotations;

import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@Operation(summary = "Criar um novo usuário",
        description = "Realiza a criação de um novo usuário no sistema", tags = {"User"})
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "Criado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = OutputDto.class)
                        )),
                @ApiResponse(responseCode = "400", description = "Requisição inválida",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = OutputDto.class)
                        )),
                @ApiResponse(responseCode = "401", description = "Não autorizado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = OutputDto.class)
                        )),
                @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = OutputDto.class)
                        ))
        }
)
public @interface UserCreateSwaggerDoc {}