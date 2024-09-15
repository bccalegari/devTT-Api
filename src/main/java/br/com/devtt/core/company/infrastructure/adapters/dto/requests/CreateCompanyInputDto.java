package br.com.devtt.core.company.infrastructure.adapters.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CreateCompanyInputDto {
    @NotNull(message = "O campo nome é obrigatório")
    @NotEmpty(message = "O campo nome não pode ser vazio")
    private final String name;

    @NotNull(message = "O campo CNPJ é obrigatório")
    @NotEmpty(message = "O campo CNPJ não pode ser vazio")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 caracteres")
    private final String cnpj;
}