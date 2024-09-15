package br.com.devtt.core.company.application.strategies;

import br.com.devtt.core.company.abstractions.application.strategies.GetCompanyFilterStrategy;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("SpringGetCompanyNameFilterStrategy")
public class SpringGetCompanyNameFilterStrategy implements GetCompanyFilterStrategy {
    private final CompanyRepository<CompanyEntity> companyRepository;

    @Autowired
    public SpringGetCompanyNameFilterStrategy(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository
    ) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<String> execute() {
        return companyRepository.getAllNames();
    }
}