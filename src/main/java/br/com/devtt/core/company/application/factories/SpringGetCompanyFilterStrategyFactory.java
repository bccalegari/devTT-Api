package br.com.devtt.core.company.application.factories;

import br.com.devtt.core.company.abstractions.application.factories.GetCompanyFilterStrategyFactory;
import br.com.devtt.core.company.abstractions.application.strategies.GetCompanyFilterStrategy;
import br.com.devtt.core.company.domain.valueobjects.CompanyFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Qualifier("SpringGetCompanyFilterStrategyFactory")
public class SpringGetCompanyFilterStrategyFactory implements GetCompanyFilterStrategyFactory {
    private Map<CompanyFilter, GetCompanyFilterStrategy> strategies;

    public SpringGetCompanyFilterStrategyFactory(
            @Qualifier("SpringGetCompanyNameFilterStrategy") GetCompanyFilterStrategy getCompanyNameFilterStrategy,
            @Qualifier("SpringGetCompanyCnpjFilterStrategy") GetCompanyFilterStrategy getCompanyCnpjFilterStrategy
    ) {
        this.strategies = Map.of(
                CompanyFilter.NAME, getCompanyNameFilterStrategy,
                CompanyFilter.CNPJ, getCompanyCnpjFilterStrategy
        );
    }

    @Override
    public GetCompanyFilterStrategy execute(CompanyFilter companyFilter) {
        return strategies.get(companyFilter);
    }
}