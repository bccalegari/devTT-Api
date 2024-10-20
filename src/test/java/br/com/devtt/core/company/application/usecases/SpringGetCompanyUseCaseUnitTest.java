package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyOutputDtoMapper;
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
    void shouldGetCompany() {
        when(companyRepository.findById(1)).thenReturn(Optional.of(companyEntity));
        when(companyMapper.toDomain(companyEntity)).thenReturn(company);
        when(responseMapper.toDto(company)).thenReturn(companyOutputDto);

        var companyOutputDto = springGetCompanyUseCase.execute(1);

        assertNotNull(companyOutputDto);
        assertEquals(1, companyOutputDto.id());
        assertEquals("Test Company", companyOutputDto.name());
        assertEquals("12345678901234", companyOutputDto.cnpj());

        verify(companyRepository, times(1)).findById(1);
        verify(companyMapper, times(1)).toDomain(companyEntity);
        verify(responseMapper, times(1)).toDto(company);
    }

    @Test
    void shouldThrowCompanyNotFoundException() {
        when(companyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> springGetCompanyUseCase.execute(1));

        verify(companyRepository, times(1)).findById(1);
        verifyNoInteractions(companyMapper, responseMapper);
    }
}