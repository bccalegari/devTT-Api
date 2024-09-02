package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.UserLoginUseCase;
import br.com.devtt.infrastructure.adapters.controllers.swagger.annotations.AuthLoginSwaggerDoc;
import br.com.devtt.infrastructure.adapters.dto.requests.AuthLoginInputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.AuthLoginOutputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
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
    private final UserLoginUseCase loginUseCase;

    @Autowired
    public SpringAuthController(
            @Qualifier("SpringUserLoginUseCase") UserLoginUseCase loginUseCase
    ) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    @AuthLoginSwaggerDoc
    public ResponseEntity<OutputDto> login(@Valid @RequestBody AuthLoginInputDto inputDto) {
        var token = loginUseCase.execute(inputDto.getEmail(), inputDto.getPassword());

        var outputDto = new AuthLoginOutputDto("Login realizado com sucesso!",
                token.getValue());

        int i = 2 + 2;

        return ResponseEntity.ok(outputDto);
    }
}