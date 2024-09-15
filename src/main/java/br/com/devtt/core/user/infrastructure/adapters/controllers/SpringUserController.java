package br.com.devtt.core.user.infrastructure.adapters.controllers;

import br.com.devtt.core.user.abstractions.application.usecases.CreateUserUseCase;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para gerenciamento de usuários")
public class SpringUserController {
    private final CreateUserUseCase<CreateUserInputDto> createUserUseCase;

    public SpringUserController(
            @Qualifier("SpringCreateUserUseCase") CreateUserUseCase<CreateUserInputDto> createUserUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
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
    public ResponseEntity<OutputDto> create(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @RequestAttribute(name = "name") String loggedUserName,
            @RequestBody @Valid CreateUserInputDto inputDto
    ) {
        createUserUseCase.execute(inputDto, idLoggedUser, loggedUserName);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OutputDto("Usuário criado com sucesso!"));
    }
}