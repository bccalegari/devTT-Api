package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.CreateCompanyUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.company.application.exceptions.CompanyAlreadyExistsException;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringCreateCompanyUseCase")
public class SpringCreateCompanyUseCase implements CreateCompanyUseCase {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final DomainMapper<Company, CompanyEntity> companyMapper;

    @Autowired
    public SpringCreateCompanyUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            CompanyMapper companyMapper
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    @Transactional
    public void execute(String name, String cnpj, Long idLoggedUser) {
        var companyDomain = Company.builder()
                .name(name)
                .cnpj(new Cnpj(cnpj))
                .auditing(Auditing.builder().createdBy(idLoggedUser).build())
                .build();

        var companyExists = companyRepository.findByCnpj(cnpj).isPresent();

        if (companyExists) {
            throw new CompanyAlreadyExistsException("A empresa já existe, não é possível cadastrá-la novamente.");
        }

        var companyEntity = companyMapper.toEntity(companyDomain);
        companyRepository.save(companyEntity);
    }
}