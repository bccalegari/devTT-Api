package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.AbstractPagedOutputDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@SuperBuilder
@Getter
@EqualsAndHashCode(of = "companies", callSuper = false)
public class GetAllCompaniesOutputDto extends AbstractPagedOutputDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6154341971273321968L;
    private final List<GetCompanyOutputDto> companies;

    @JsonCreator
    public GetAllCompaniesOutputDto(
            @JsonProperty("companies") List<GetCompanyOutputDto> companies,
            @JsonProperty("page") int page,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") long totalElements,
            @JsonProperty("totalPages") long totalPages
    ) {
        super(page, size, totalElements, totalPages);
        this.companies = companies;
    }
}