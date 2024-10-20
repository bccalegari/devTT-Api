package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.GetAllCompaniesUseCase;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetAllCompaniesOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyOutputDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("SpringGetAllCompaniesUseCase")
public class SpringGetAllCompaniesUseCase implements GetAllCompaniesUseCase<GetAllCompaniesOutputDto> {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final DomainMapper<Company, CompanyEntity> companyMapper;
    private final AdapterMapper<Company, GetCompanyOutputDto> responseMapper;

    @Autowired
    public SpringGetAllCompaniesUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            CompanyMapper companyMapper,
            GetCompanyOutputDtoMapper responseMapper
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public GetAllCompaniesOutputDto execute(String name, String cnpj, Integer page, Integer size) {
        Page<CompanyEntity> companyEntityPage;
        var paginationParams = new PaginationParams(page, size);

        if (name == null && cnpj == null) {
            companyEntityPage = companyRepository.findAll(paginationParams);
        } else {
            companyEntityPage = companyRepository.findAllByNameOrCnpj(name, cnpj, paginationParams);
        }

        if (companyEntityPage.getContent().isEmpty()) {
            return new GetAllCompaniesOutputDto(
                    paginationParams.getCurrentPage(), paginationParams.getSize(), 0L, 0L, List.of()
            );
        }

        var companyDomainList = companyEntityPage.getContent().stream()
                .map(companyMapper::toDomain)
                .toList();

        var companiesDtoList = companyDomainList.stream()
                .map(responseMapper::toDto)
                .toList();

        return new GetAllCompaniesOutputDto(
                paginationParams.getCurrentPage(), paginationParams.getSize(),
                companyEntityPage.getTotalElements(), companyEntityPage.getTotalPages(), companiesDtoList
        );
    }
}