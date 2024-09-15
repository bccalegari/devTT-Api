package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.GetCompanyUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyResponseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringGetCompanyUseCase")
public class SpringGetCompanyUseCase implements GetCompanyUseCase<GetCompanyOutputDto> {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final DomainMapper<Company, CompanyEntity> companyMapper;
    private final AdapterMapper<Company, GetCompanyOutputDto> responseMapper;

    @Autowired
    public SpringGetCompanyUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            CompanyMapper companyMapper,
            GetCompanyResponseDtoMapper responseMapper
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public GetCompanyOutputDto execute(Integer id) {
        var companyEntity = companyRepository.findById(id);

        if(companyEntity.isEmpty()) {
            throw new CompanyNotFoundException("A empresa não foi encontrada.");
        }

        var companyDomain = companyMapper.toDomain(companyEntity.get());
        return responseMapper.toDto(companyDomain);
    }
}