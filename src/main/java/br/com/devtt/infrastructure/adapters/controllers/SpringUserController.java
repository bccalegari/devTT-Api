package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.CreateUserUseCase;
import br.com.devtt.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.LoggedInUserInfo;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para obter informações dos usuários")
public class SpringUserController {

    @Autowired
    @Qualifier("SpringCreateUserUseCase")
    CreateUserUseCase<CreateUserInputDto> createUserUseCase;

    @GetMapping("/info")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Informações do usuário logado",
            description = "Retorna informações do usuário logado", tags = {"User"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoggedInUserInfo.class)
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
    public ResponseEntity<LoggedInUserInfo> getLoggedInUserInfo(HttpServletRequest request) {
        String name = request.getAttribute("name").toString();
        String role = request.getAttribute("role").toString();

        LoggedInUserInfo loggedInUserInfo = new LoggedInUserInfo(name, role);

        System.out.println("fdsfds");

        return ResponseEntity.ok(loggedInUserInfo);
    }

    @PostMapping("/create")
    public void postNewUser(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @RequestBody @Valid CreateUserInputDto inputDto
    ) {
        createUserUseCase.create(inputDto, idLoggedUser);
    }
}