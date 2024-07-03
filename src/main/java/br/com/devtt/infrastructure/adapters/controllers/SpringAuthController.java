package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.UserLoginUseCase;
import br.com.devtt.core.abstractions.domain.valueobjects.Token;
import br.com.devtt.infrastructure.adapters.dto.requests.AuthLoginInputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.AuthLoginOutputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoint para autenticação")
public class SpringAuthController {
    @Autowired
    @Qualifier("SpringUserLoginUseCase")
    private UserLoginUseCase loginUseCase;

    @PostMapping("/login")
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
    public ResponseEntity<OutputDto> login(@Valid @RequestBody AuthLoginInputDto inputDto) {
        Token token = loginUseCase.execute(inputDto.getEmail(), inputDto.getPassword());

        AuthLoginOutputDto outputDto = new AuthLoginOutputDto("Login realizado com sucesso!",
                token.getToken());

        return ResponseEntity.ok(outputDto);
    }
}