package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.UserLoginUseCase;
import br.com.devtt.core.abstractions.domain.valueobjects.Token;
import br.com.devtt.infrastructure.adapters.dto.requests.AuthLoginInputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.AuthLoginOutputDto;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SpringAuthController {
    @Autowired
    @Qualifier("SpringUserLoginUseCase")
    private UserLoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<OutputDto> login(@Valid @RequestBody AuthLoginInputDto inputDto) {
        Token token = loginUseCase.execute(inputDto.getEmail(), inputDto.getPassword());

        AuthLoginOutputDto outputDto = new AuthLoginOutputDto("User logged in successfully",
                token.getToken());

        return ResponseEntity.ok(outputDto);
    }
}