package br.com.devtt.infrastructure.adapters.controllers;

import br.com.devtt.core.abstractions.application.usecases.CreateCompanyUseCase;
import br.com.devtt.core.abstractions.application.usecases.UpdateCompanyUseCase;
import br.com.devtt.infrastructure.adapters.controllers.swagger.annotations.CompanyCreateSwaggerDoc;
import br.com.devtt.infrastructure.adapters.controllers.swagger.annotations.CompanyUpdateSwaggerDoc;
import br.com.devtt.infrastructure.adapters.dto.requests.CreateCompanyInputDto;
import br.com.devtt.infrastructure.adapters.dto.requests.UpdateCompanyInputDto;
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
@RequestMapping("/company")
@Tag(name = "Company", description = "Endpoints para gerenciamento de empresas")
public class SpringCompanyController {
    private final CreateCompanyUseCase createCompanyUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;

    public SpringCompanyController(
            @Qualifier("SpringCreateCompanyUseCase") CreateCompanyUseCase createCompanyUseCase,
            @Qualifier("SpringUpdateCompanyUseCase") UpdateCompanyUseCase updateCompanyUseCase
    ) {
        this.createCompanyUseCase = createCompanyUseCase;
        this.updateCompanyUseCase = updateCompanyUseCase;
    }

    @PostMapping("/create")
    @CompanyCreateSwaggerDoc
    public ResponseEntity<OutputDto> create(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @RequestBody @Valid CreateCompanyInputDto inputDto
    ) {
        createCompanyUseCase.create(inputDto.getName(), inputDto.getCnpj(), idLoggedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OutputDto("Empresa criada com sucesso!"));
    }

    @PutMapping("/update/{id}")
    @CompanyUpdateSwaggerDoc
    public ResponseEntity<OutputDto> update(
            @RequestAttribute(name = "idUser") Long idLoggedUser,
            @PathVariable Integer id,
            @RequestBody @Valid UpdateCompanyInputDto inputDto
    ) {
        updateCompanyUseCase.update(id, inputDto.getName(), inputDto.getCnpj(), idLoggedUser);
        return ResponseEntity.ok(new OutputDto("Empresa atualizada com sucesso!"));
    }
}