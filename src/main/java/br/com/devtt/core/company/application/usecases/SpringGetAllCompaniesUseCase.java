package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.GetAllCompaniesUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.gateway.cache.CompanyCacheKeys;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetAllCompaniesOutputOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("SpringGetAllCompaniesUseCase")
public class SpringGetAllCompaniesUseCase implements GetAllCompaniesUseCase<GetAllCompaniesOutputOutputDto> {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final CacheGateway cacheGateway;
    private final DomainMapper<Company, CompanyEntity> companyMapper;
    private final AdapterMapper<Company, GetCompanyOutputDto> responseMapper;

    @Autowired
    public SpringGetAllCompaniesUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            @Qualifier("RedisCacheGateway") CacheGateway cacheGateway,
            CompanyMapper companyMapper,
            GetCompanyOutputDtoMapper responseMapper
    ) {
        this.companyRepository = companyRepository;
        this.cacheGateway = cacheGateway;
        this.companyMapper = companyMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public GetAllCompaniesOutputOutputDto execute(String name, String cnpj, Integer page, Integer size) {
        var companiesFromCache = cacheGateway
                .get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(name, cnpj, page, size));

        if (companiesFromCache != null) {
            return (GetAllCompaniesOutputOutputDto) companiesFromCache;
        }

        Page<CompanyEntity> companyEntityPage;
        var paginationParams = new PaginationParams(page, size);

        if (name == null && cnpj == null) {
            companyEntityPage = companyRepository.findAll(paginationParams);
        } else {
            companyEntityPage = companyRepository.findAllByNameOrCnpj(name, cnpj, paginationParams);
        }

        if (companyEntityPage.getContent().isEmpty()) {
            return GetAllCompaniesOutputOutputDto.builder()
                    .currentPage(paginationParams.getCurrentPage())
                    .size(paginationParams.getSize())
                    .totalElements(0L)
                    .totalPages(0L)
                    .companies(List.of())
                    .build();
        }

        var companyDomainList = companyEntityPage.getContent().stream()
                .map(companyMapper::toDomain)
                .toList();

        var companiesDtoList = companyDomainList.stream()
                .map(responseMapper::toDto)
                .toList();

        var companiesOutputDto = GetAllCompaniesOutputOutputDto.builder()
                .currentPage(paginationParams.getCurrentPage())
                .size(paginationParams.getSize())
                .totalElements(companyEntityPage.getTotalElements())
                .totalPages(companyEntityPage.getTotalPages())
                .companies(companiesDtoList)
                .build();

        cacheGateway.put(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(name, cnpj, page, size), companiesOutputDto);

        return companiesOutputDto;
    }
}