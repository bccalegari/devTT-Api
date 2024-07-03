package br.com.devtt.infrastructure.adapters.controllers;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para obter informações dos usuários")
public class SpringUserController {

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

        return ResponseEntity.ok(loggedInUserInfo);
    }
}