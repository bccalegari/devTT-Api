package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.CreateUserUseCase;
import br.com.devtt.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para gerenciamento de usuários")
public class SpringUserController {

    @Autowired
    @Qualifier("SpringCreateUserUseCase")
    private CreateUserUseCase<CreateUserInputDto> createUserUseCase;

    @PostMapping("/create")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Criar um novo usuário",
            description = "Realiza a criação de um novo usuário no sistema", tags = {"User"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
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
    public ResponseEntity<OutputDto> postNewUser(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @RequestBody @Valid CreateUserInputDto inputDto
    ) {
        createUserUseCase.create(inputDto, idLoggedUser);
        return ResponseEntity.ok(new OutputDto("Usuário criado com sucesso!"));

    }
}