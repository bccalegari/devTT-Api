package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.gateway.cache.CompanyCacheKeys;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringGetCompanyUseCaseUnitTest {
    @InjectMocks private SpringGetCompanyUseCase springGetCompanyUseCase;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private CompanyMapper companyMapper;
    @Mock private GetCompanyOutputDtoMapper responseMapper;
    @Mock private CacheGateway cacheGateway;
    @Mock private ObjectMapper objectMapper;

    private CompanyEntity companyEntity;
    private Company company;
    private GetCompanyOutputDto companyOutputDto;

    @BeforeEach
    void setUp() {
        companyEntity = CompanyEntity.builder()
                .id(1)
                .name("Test Company")
                .cnpj("12345678901234")
                .build();

        company = Company.builder()
                .id(1)
                .name("Test Company")
                .cnpj(new Cnpj("12345678901234"))
                .build();

        companyOutputDto = new GetCompanyOutputDto(1, "Test Company", "12345678901234");
    }

    @Test
    void shouldGetCompanyFromCache() {
        when(cacheGateway.get(CompanyCacheKeys.COMPANY.getKey().formatted(1))).thenReturn(companyOutputDto);
        when(objectMapper.convertValue(companyOutputDto, GetCompanyOutputDto.class)).thenReturn(companyOutputDto);

        var companyOutputDto = springGetCompanyUseCase.execute(1);

        assertNotNull(companyOutputDto);
        assertEquals(1, companyOutputDto.id());
        assertEquals("Test Company", companyOutputDto.name());
        assertEquals("12345678901234", companyOutputDto.cnpj());

        verify(cacheGateway, times(1)).get(CompanyCacheKeys.COMPANY.getKey().formatted(1));
        verifyNoMoreInteractions(cacheGateway);
        verifyNoInteractions(companyRepository, companyMapper, responseMapper);
    }

    @Test
    void shouldGetCompany() {
        when(cacheGateway.get(CompanyCacheKeys.COMPANY.getKey().formatted(1))).thenReturn(null);
        when(companyRepository.findById(1)).thenReturn(Optional.of(companyEntity));
        when(companyMapper.toDomain(companyEntity)).thenReturn(company);
        when(responseMapper.toDto(company)).thenReturn(companyOutputDto);
        doNothing().when(cacheGateway).put(CompanyCacheKeys.COMPANY.getKey().formatted(1), companyOutputDto);

        var companyOutputDto = springGetCompanyUseCase.execute(1);

        assertNotNull(companyOutputDto);
        assertEquals(1, companyOutputDto.id());
        assertEquals("Test Company", companyOutputDto.name());
        assertEquals("12345678901234", companyOutputDto.cnpj());

        verify(cacheGateway, times(1)).get(CompanyCacheKeys.COMPANY.getKey().formatted(1));
        verify(companyRepository, times(1)).findById(1);
        verify(companyMapper, times(1)).toDomain(companyEntity);
        verify(responseMapper, times(1)).toDto(company);
        verify(cacheGateway, times(1)).put(CompanyCacheKeys.COMPANY.getKey().formatted(1), companyOutputDto);
    }

    @Test
    void shouldThrowCompanyNotFoundException() {
        when(cacheGateway.get(CompanyCacheKeys.COMPANY.getKey().formatted(1))).thenReturn(null);
        when(companyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> springGetCompanyUseCase.execute(1));

        verify(cacheGateway, times(1)).get(CompanyCacheKeys.COMPANY.getKey().formatted(1));
        verify(companyRepository, times(1)).findById(1);
        verifyNoMoreInteractions(cacheGateway, companyRepository);
        verifyNoInteractions(companyMapper, responseMapper);
    }
}