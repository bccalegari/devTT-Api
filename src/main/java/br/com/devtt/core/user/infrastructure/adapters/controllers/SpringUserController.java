package br.com.devtt.core.user.infrastructure.adapters.controllers;

import br.com.devtt.core.user.abstractions.application.usecases.*;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.UpdateUserInputDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetAllUsersOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para gerenciamento de usuários")
public class SpringUserController {
    private final GetAllUsersUseCase<GetAllUsersOutputDto> getAllUsersUseCase;
    private final GetUserUseCase<Long, GetUserOutputDto> getUserUseCase;
    private final CreateUserUseCase<CreateUserInputDto> createUserUseCase;
    private final UpdateUserUseCase<UpdateUserInputDto> updateUserUseCase;
    private final DeleteUserUseCase<Long> deleteUserUseCase;

    public SpringUserController(
            @Qualifier("SpringGetAllUsersUseCase") GetAllUsersUseCase<GetAllUsersOutputDto> getAllUsersUseCase,
            @Qualifier("SpringGetUserUseCase") GetUserUseCase<Long, GetUserOutputDto> getUserUseCase,
            @Qualifier("SpringCreateUserUseCase") CreateUserUseCase<CreateUserInputDto> createUserUseCase,
            @Qualifier("SpringUpdateUserUseCase") UpdateUserUseCase<UpdateUserInputDto> updateUserUseCase,
            @Qualifier("SpringDeleteUserUseCase") DeleteUserUseCase<Long> deleteUserUseCase
    ) {
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.getUserUseCase = getUserUseCase;
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @GetMapping
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados no sistema", tags = {"User"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetAllUsersOutputDto.class)
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
    public ResponseEntity<GetAllUsersOutputDto> getAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer idCompany,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestAttribute(name = "role") String loggedUserRole,
            @RequestAttribute(name = "idCompany") Integer loggedUserCompanyId
    ) {
        var users = getAllUsersUseCase.execute(page, size, idCompany, search, loggedUserRole, loggedUserCompanyId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um usuário",
            description = "Retorna um usuário específico", tags = {"User"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetUserOutputDto.class)
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
    public ResponseEntity<GetUserOutputDto> get(
            @PathVariable Long id,
            @RequestAttribute(name = "idUser") Long loggedUserId,
            @RequestAttribute(name = "role") String loggedUserRole,
            @RequestAttribute(name = "idCompany") Integer loggedUserCompanyId
    ) {
        var user = getUserUseCase.execute(id, loggedUserId, loggedUserRole, loggedUserCompanyId);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
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

    @PutMapping("/{id}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "Atualizar um usuário",
            description = "Realiza a atualização de um usuário no sistema", tags = {"User"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deletado",
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
    public ResponseEntity<OutputDto> update(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @RequestAttribute(name = "role") String loggedUserRole,
            @RequestAttribute(name = "idCompany") Integer loggedUserCompanyId,
            @PathVariable(name = "id") Long userToBeUpdatedId,
            @RequestBody @Valid UpdateUserInputDto inputDto
    ) {
        updateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);
        return ResponseEntity.ok(new OutputDto("Usuário atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "Deletar um usuário",
            description = "Realiza a deleção de um usuário no sistema", tags = {"User"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deletado",
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
    public ResponseEntity<OutputDto> delete(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @PathVariable Long id
    ) {
        deleteUserUseCase.execute(id, idLoggedUser);
        return ResponseEntity.ok(new OutputDto("Usuário deletado com sucesso!"));
    }
}