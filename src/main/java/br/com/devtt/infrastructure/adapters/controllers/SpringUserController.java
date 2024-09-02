package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.CreateUserUseCase;
import br.com.devtt.infrastructure.adapters.controllers.swagger.annotations.UserCreateSwaggerDoc;
import br.com.devtt.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
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

    @PostMapping("/create")
    @UserCreateSwaggerDoc
    public ResponseEntity<OutputDto> create(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @RequestAttribute(name = "name") String loggedUserName,
            @RequestBody @Valid CreateUserInputDto inputDto
    ) {
        createUserUseCase.create(inputDto, idLoggedUser, loggedUserName);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OutputDto("Usuário criado com sucesso!"));
    }
}