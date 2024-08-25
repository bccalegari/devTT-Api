package br.com.devtt.infrastructure.adapters.controllers.swagger.annotations;

import br.com.devtt.infrastructure.adapters.dto.responses.AuthLoginOutputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Operation(summary = "Login", description = "Realiza o login de um usuário", tags = {"Auth"})
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "OK",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = AuthLoginOutputDto.class)
                        )),
                @ApiResponse(responseCode = "400", description = "Erro na requisição",
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
public @interface AuthLoginSwaggerDoc {}