package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetAllCompaniesOutputOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.gateway.cache.CompanyCacheKeys;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PageImpl;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringGetAllCompaniesUseCaseUnitTest {
    @InjectMocks private SpringGetAllCompaniesUseCase springGetAllCompaniesUseCase;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private CompanyMapper companyMapper;
    @Mock private GetCompanyOutputDtoMapper responseMapper;
    @Mock private CacheGateway cacheGateway;

    private String name;
    private String cnpj;
    private Integer page = 0;
    private Integer size = 10;
    private PaginationParams paginationParams;
    private CompanyEntity companyEntity;
    private List<CompanyEntity> companyEntityList;
    private Company company;
    private GetCompanyOutputDto companyOutputDto;
    private GetAllCompaniesOutputOutputDto companiesOutputDto;

    @BeforeEach
    void setUp() {
        paginationParams = new PaginationParams(page, size);
        companyEntity = CompanyEntity.builder()
                .id(1)
                .name("Company")
                .cnpj("12345678901234")
                .build();
        companyEntityList = List.of(companyEntity);
        company = Company.builder()
                .id(1)
                .name("Company")
                .cnpj(new Cnpj("12345678901234"))
                .build();
        companyOutputDto = new GetCompanyOutputDto(1, "Company", "12345678901234");
        companiesOutputDto = GetAllCompaniesOutputOutputDto.builder()
                .currentPage(page)
                .size(size)
                .totalPages(1L)
                .totalElements(1L)
                .companies(List.of(companyOutputDto))
                .build();
    }

    @Test
    void shouldReturnAllCompaniesFromCache() {
        name = null;
        cnpj = null;

        when(cacheGateway.get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(null, null, page, size)))
                .thenReturn(companiesOutputDto);

        var outputDto = springGetAllCompaniesUseCase.execute(name, cnpj, page, size);

        assertNotNull(outputDto);
        assertEquals(companiesOutputDto, outputDto);

        verify(cacheGateway).get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(null, null, page, size));
        verifyNoMoreInteractions(cacheGateway);
        verifyNoInteractions(companyRepository, companyMapper, responseMapper);
    }

    @Test
    void shouldReturnAllCompaniesWhithoutFilter() {
        name = null;
        cnpj = null;

        when(cacheGateway.get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(null, null, page, size)))
                .thenReturn(null);
        when(companyRepository.findAll(paginationParams))
                .thenReturn(new PageImpl<>(page, size, 0, 1L, 1L, companyEntityList));
        when(companyMapper.toDomain(companyEntity)).thenReturn(company);
        when(responseMapper.toDto(company)).thenReturn(companyOutputDto);
        doNothing().when(cacheGateway).put(CompanyCacheKeys.COMPANIES_PAGED.getKey()
                .formatted(null, null, page, size), companiesOutputDto);

        var outputDto = springGetAllCompaniesUseCase.execute(name, cnpj, page, size);

        assertNotNull(outputDto);
        assertEquals(companiesOutputDto, outputDto);

        verify(cacheGateway).get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(null, null, page, size));
        verify(companyRepository).findAll(paginationParams);
        verify(companyMapper).toDomain(companyEntity);
        verify(responseMapper).toDto(company);
        verify(cacheGateway).put(CompanyCacheKeys.COMPANIES_PAGED.getKey()
                .formatted(null, null, page, size), companiesOutputDto);
    }

    @Test
    void shouldReturnAllCompaniesWithFilter() {
        name = "Company";
        cnpj = "12345678901234";

        when(cacheGateway.get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(name, cnpj, page, size)))
                .thenReturn(null);
        when(companyRepository.findAllByNameOrCnpj(name, cnpj, paginationParams))
                .thenReturn(new PageImpl<>(page, size, 0, 1L, 1L, companyEntityList));
        when(companyMapper.toDomain(companyEntity)).thenReturn(company);
        when(responseMapper.toDto(company)).thenReturn(companyOutputDto);
        doNothing().when(cacheGateway).put(CompanyCacheKeys.COMPANIES_PAGED.getKey()
                .formatted(name, cnpj, page, size), companiesOutputDto);

        var outputDto = springGetAllCompaniesUseCase.execute(name, cnpj, page, size);

        assertNotNull(outputDto);
        assertEquals(companiesOutputDto, outputDto);

        verify(cacheGateway).get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(name, cnpj, page, size));
        verify(companyRepository).findAllByNameOrCnpj(name, cnpj, paginationParams);
        verify(companyMapper).toDomain(companyEntity);
        verify(responseMapper).toDto(company);
        verify(cacheGateway).put(CompanyCacheKeys.COMPANIES_PAGED.getKey()
                .formatted(name, cnpj, page, size), companiesOutputDto);
    }

    @Test
    void shouldReturnEmptyListWhenNoCompanyIsFound() {
        name = null;
        cnpj = null;
        companiesOutputDto = GetAllCompaniesOutputOutputDto.builder()
                .currentPage(page)
                .size(size)
                .totalPages(0L)
                .totalElements(0L)
                .companies(List.of())
                .build();

        when(cacheGateway.get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(null, null, page, size)))
                .thenReturn(null);
        when(companyRepository.findAll(paginationParams))
                .thenReturn(new PageImpl<>(page, size, 0, 0L, 0L, List.of()));

        var outputDto = springGetAllCompaniesUseCase.execute(name, cnpj, page, size);

        assertNotNull(outputDto);
        assertEquals(companiesOutputDto, outputDto);

        verify(cacheGateway).get(CompanyCacheKeys.COMPANIES_PAGED.getKey().formatted(null, null, page, size));
        verify(companyRepository).findAll(paginationParams);
        verifyNoMoreInteractions(cacheGateway);
        verifyNoInteractions(companyMapper, responseMapper);
    }
}