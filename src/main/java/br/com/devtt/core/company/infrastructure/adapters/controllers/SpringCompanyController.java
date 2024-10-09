package br.com.devtt.core.company.infrastructure.adapters.controllers;

import br.com.devtt.core.company.abstractions.application.usecases.*;
import br.com.devtt.core.company.infrastructure.adapters.dto.requests.CreateCompanyInputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.requests.UpdateCompanyInputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetAllCompaniesOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyFilterOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.OutputDto;
import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/company")
@Secured("ROLE_ADMIN")
@Tag(name = "Company", description = "Endpoints para gerenciamento de empresas")
public class SpringCompanyController {
    private final CreateCompanyUseCase createCompanyUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;
    private final GetAllCompaniesUseCase<GetAllCompaniesOutputDto> getAllCompaniesUseCase;
    private final GetCompanyUseCase<GetCompanyOutputDto> getCompanyUseCase;
    private final DeleteCompanyUseCase deleteCompanyUseCase;
    private final GetCompanyFilterUseCase<GetCompanyFilterOutputDto> getCompanyFilterUseCase;

    public SpringCompanyController(
            @Qualifier("SpringCreateCompanyUseCase") CreateCompanyUseCase createCompanyUseCase,
            @Qualifier("SpringUpdateCompanyUseCase") UpdateCompanyUseCase updateCompanyUseCase,
            @Qualifier("SpringGetAllCompaniesUseCase") GetAllCompaniesUseCase<GetAllCompaniesOutputDto> getAllCompaniesUseCase,
            @Qualifier("SpringGetCompanyUseCase") GetCompanyUseCase<GetCompanyOutputDto> getCompanyUseCase,
            @Qualifier("SpringDeleteCompanyUseCase") DeleteCompanyUseCase deleteCompanyUseCase,
            @Qualifier("SpringGetCompanyFilterUseCase") GetCompanyFilterUseCase<GetCompanyFilterOutputDto> getCompanyFilterUseCase
    ) {
        this.createCompanyUseCase = createCompanyUseCase;
        this.updateCompanyUseCase = updateCompanyUseCase;
        this.getAllCompaniesUseCase = getAllCompaniesUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
        this.deleteCompanyUseCase = deleteCompanyUseCase;
        this.getCompanyFilterUseCase = getCompanyFilterUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova empresa",
            description = "Realiza a criação de uma nova empresa no sistema", tags = {"Company"})
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
            @RequestBody @Valid CreateCompanyInputDto inputDto
    ) {
        createCompanyUseCase.execute(inputDto.getName(), inputDto.getCnpj(), idLoggedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OutputDto("Empresa criada com sucesso!"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma empresa",
            description = "Realiza a atualização de uma empresa no sistema.", tags = {"Company"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
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
            @PathVariable Integer id,
            @RequestBody @Valid UpdateCompanyInputDto inputDto
    ) {
        updateCompanyUseCase.execute(id, inputDto.getName(), inputDto.getCnpj(), idLoggedUser);
        return ResponseEntity.ok(new OutputDto("Empresa atualizada com sucesso!"));
    }

    @GetMapping
    @Operation(summary = "Listar empresas",
            description = "Lista todas as empresas cadastradas no sistema", tags = {"Company"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
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
                    @ApiResponse(responseCode = "404", description = "Não encontrado",
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
    public ResponseEntity<GetAllCompaniesOutputDto> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        var companies = getAllCompaniesUseCase.execute(name, cnpj, page, size);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa por id",
            description = "Busca uma empresa cadastrada no sistema pelo id", tags = {"Company"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
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
                    @ApiResponse(responseCode = "404", description = "Não encontrado",
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
    public ResponseEntity<GetCompanyOutputDto> getById(
            @PathVariable Integer id
    ) {
        var company = getCompanyUseCase.execute(id);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar empresa",
            description = "Deleta uma empresa cadastrada no sistema", tags = {"Company"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK",
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
                    @ApiResponse(responseCode = "404", description = "Não encontrado",
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
            @PathVariable Integer id
    ) {
        deleteCompanyUseCase.execute(id, idLoggedUser);
        return ResponseEntity.ok(new OutputDto("Empresa deletada com sucesso!"));
    }


    @GetMapping("/name-filter-list")
    @Hidden
    public ResponseEntity<GetCompanyFilterOutputDto> getNameFilterList() {
        return ResponseEntity.ok(getCompanyFilterUseCase.execute("name"));

    }

    @GetMapping("/cnpj-filter-list")
    @Hidden
    public ResponseEntity<GetCompanyFilterOutputDto> getCnpjFilterList() {
        return ResponseEntity.ok(getCompanyFilterUseCase.execute("cnpj"));
    }
}