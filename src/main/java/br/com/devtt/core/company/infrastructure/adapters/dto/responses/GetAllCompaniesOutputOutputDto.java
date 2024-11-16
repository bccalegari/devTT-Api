package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.AbstractPagedOutputDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@SuperBuilder
@Getter
@EqualsAndHashCode(of = "companies", callSuper = false)
public class GetAllCompaniesOutputOutputDto extends AbstractPagedOutputDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6154341971273321968L;
    private final List<GetCompanyOutputDto> companies;
}