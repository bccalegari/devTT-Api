package br.com.devtt.core.company.abstractions.application.factories;

import br.com.devtt.core.company.abstractions.application.strategies.GetCompanyFilterStrategy;
import br.com.devtt.core.company.domain.valueobjects.CompanyFilter;

public interface GetCompanyFilterStrategyFactory {
    GetCompanyFilterStrategy execute(CompanyFilter companyFilter);
}