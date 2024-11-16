package br.com.devtt.core.company.infrastructure.adapters.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UpdateCompanyInputDto {
    @Pattern(regexp = "^$|\\S+.*", message = "O campo nome não pode ser vazio")
    private final String name;

    @Pattern(regexp = "^$|\\S+.*", message = "O campo CNPJ não pode ser vazio")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 caracteres")
    @Schema(example = "11111111111111")
    private final String cnpj;
}