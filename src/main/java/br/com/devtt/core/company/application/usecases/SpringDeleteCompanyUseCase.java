package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.DeleteCompanyUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringDeleteCompanyUseCase")
public class SpringDeleteCompanyUseCase implements DeleteCompanyUseCase {
    private final CompanyRepository<CompanyEntity> companyRepository;

    @Autowired
    public SpringDeleteCompanyUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository
    ) {
        this.companyRepository = companyRepository;
    }


    @Override
    @Transactional
    public void execute(Integer id, Long idLoggedUser) {
        var companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("A empresa não foi encontrada."));

        companyEntity.setUpdatedBy(idLoggedUser);
        companyEntity.setDeletedBy(idLoggedUser);

        companyRepository.delete(companyEntity);
    }
}