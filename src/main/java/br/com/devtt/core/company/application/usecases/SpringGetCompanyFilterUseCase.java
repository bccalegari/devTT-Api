package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.factories.GetCompanyFilterStrategyFactory;
import br.com.devtt.core.company.abstractions.application.usecases.GetCompanyFilterUseCase;
import br.com.devtt.core.company.domain.valueobjects.CompanyFilter;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyFilterOutputDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("SpringGetCompanyFilterUseCase")
public class SpringGetCompanyFilterUseCase implements GetCompanyFilterUseCase<GetCompanyFilterOutputDto> {
    private final GetCompanyFilterStrategyFactory getCompanyFilterStrategyFactory;

    public SpringGetCompanyFilterUseCase(
            @Qualifier("SpringGetCompanyFilterStrategyFactory") GetCompanyFilterStrategyFactory getCompanyFilterStrategyFactory
    ) {
        this.getCompanyFilterStrategyFactory = getCompanyFilterStrategyFactory;
    }

    @Override
    public GetCompanyFilterOutputDto execute(String filter) {
        var companyFilter = CompanyFilter.fromString(filter);

        if (companyFilter == null) {
            return new GetCompanyFilterOutputDto(List.of());
        }

        var strategy = getCompanyFilterStrategyFactory.execute(companyFilter);

        return new GetCompanyFilterOutputDto(strategy.execute());
    }
}