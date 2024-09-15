package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.application.exceptions.CompanyAlreadyExistsException;
import br.com.devtt.enterprise.abstractions.application.services.ComparatorService;
import br.com.devtt.core.company.abstractions.application.usecases.UpdateCompanyUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringUpdateCompanyUseCase")
public class SpringUpdateCompanyUseCase implements UpdateCompanyUseCase {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final ComparatorService comparatorService;

    @Autowired
    public SpringUpdateCompanyUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            @Qualifier("ComparatorServiceImpl") ComparatorService comparatorService
    ) {
        this.companyRepository = companyRepository;
        this.comparatorService = comparatorService;
    }

    @Override
    @Transactional
    public void execute(Integer id, String name, String cnpj, Long idLoggedUser) {
       var company = companyRepository.findById(id);

        if (company.isEmpty()) {
            throw new CompanyNotFoundException("A empresa não foi encontrada.");
        }

        var companyEntity = company.get();

        var hasChanges = updateCompanyFields(companyEntity, name, cnpj);

        if (hasChanges) {
            companyEntity.setUpdatedBy(idLoggedUser);
            companyRepository.update(companyEntity);
        }
    }

    private boolean updateCompanyFields(CompanyEntity companyEntity, String name, String cnpj) {
        boolean hasChanges = false;

        if (comparatorService.hasChanges(name, companyEntity.getName())) {
            companyEntity.setName(name);
            hasChanges = true;
        }

        if (comparatorService.hasChanges(cnpj, companyEntity.getCnpj())) {
            var companyExists = companyRepository.findByCnpj(cnpj).isPresent();

            if (companyExists) {
                throw new CompanyAlreadyExistsException("A empresa já existe, não é possível cadastrá-la novamente.");
            }

            companyEntity.setCnpj(cnpj);
            hasChanges = true;
        }

        return hasChanges;
    }
}