package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyAlreadyExistsException;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import br.com.devtt.core.company.infrastructure.adapters.gateway.cache.CompanyCacheKeys;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.company.infrastructure.adapters.mappers.GetCompanyOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.services.ComparatorService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringUpdateCompanyUseCaseUnitTest {
    @InjectMocks private SpringUpdateCompanyUseCase springUpdateCompanyUseCase;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private ComparatorService comparatorService;
    @Mock private CompanyMapper domainMapper;
    @Mock private GetCompanyOutputDtoMapper adapterMapper;
    @Mock private CacheGateway cacheGateway;
    private final Integer id = 1;
    private String name = "Company";
    private String cnpj = "12345678901234";
    private final Long idLoggedUser = 1L;

    @Test
    void shouldUpdateCompanyWhenFieldsAreDifferent() {
        name = "Company Updated";

        var companyEntity = CompanyEntity.builder()
                .id(id)
                .name("Company")
                .cnpj(cnpj)
                .build();

        var companyDomainMock = mock(Company.class);
        var getCompanyOutputDtoMock = mock(GetCompanyOutputDto.class);

        when(companyRepository.findById(id)).thenReturn(Optional.of(companyEntity));
        when(comparatorService.hasChanges(name, companyEntity.getName())).thenReturn(true);
        when(comparatorService.hasChanges(cnpj, companyEntity.getCnpj())).thenReturn(false);
        doNothing().when(companyRepository).update(companyEntity);
        when(domainMapper.toDomain(companyEntity)).thenReturn(companyDomainMock);
        when(adapterMapper.toDto(companyDomainMock)).thenReturn(getCompanyOutputDtoMock);
        doNothing().when(cacheGateway).put(CompanyCacheKeys.COMPANY.getKey().formatted(id), getCompanyOutputDtoMock);
        doNothing().when(cacheGateway).deleteAllFrom(CompanyCacheKeys.COMPANIES_PATTERN.getKey());

        springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser);

        companyEntity.setUpdatedBy(idLoggedUser);

        verify(companyRepository).findById(id);
        verify(comparatorService).hasChanges(name, "Company");
        verify(comparatorService).hasChanges(cnpj, companyEntity.getCnpj());
        verify(companyRepository).update(companyEntity);
        verify(domainMapper).toDomain(companyEntity);
        verify(adapterMapper).toDto(companyDomainMock);
        verify(cacheGateway).put(CompanyCacheKeys.COMPANY.getKey().formatted(id), getCompanyOutputDtoMock);
        verify(cacheGateway).deleteAllFrom(CompanyCacheKeys.COMPANIES_PATTERN.getKey());
    }

    @Test
    void shouldNotUpdateCompanyWhenFieldsAreEqual() {
        var companyEntity = CompanyEntity.builder()
                .id(id)
                .name(name)
                .cnpj(cnpj)
                .build();

        when(companyRepository.findById(id)).thenReturn(Optional.of(companyEntity));
        when(comparatorService.hasChanges(name, companyEntity.getName())).thenReturn(false);
        when(comparatorService.hasChanges(cnpj, companyEntity.getCnpj())).thenReturn(false);

        springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser);

        verify(companyRepository).findById(id);
        verify(comparatorService).hasChanges(name, companyEntity.getName());
        verify(comparatorService).hasChanges(cnpj, companyEntity.getCnpj());
        verifyNoMoreInteractions(companyRepository, domainMapper, adapterMapper, cacheGateway);
    }

    @Test
    void shouldThrowCompanyNotFoundExceptionWhenCompanyDoesNotExist() {
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,
                () -> springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser));

        verifyNoMoreInteractions(comparatorService, companyRepository);
        verifyNoInteractions(domainMapper, adapterMapper, cacheGateway);
    }

    @Test
    void shouldThrowCompanyExistsExceptionWhenCnpjAlreadyExists() {
        cnpj = "32132123123123";

        var companyEntity = CompanyEntity.builder()
                .id(id)
                .name("Company")
                .cnpj("12345678901234")
                .build();

        when(companyRepository.findById(id)).thenReturn(Optional.of(companyEntity));
        when(comparatorService.hasChanges(name, companyEntity.getName())).thenReturn(false);
        when(comparatorService.hasChanges(cnpj, companyEntity.getCnpj())).thenReturn(true);
        when(companyRepository.findByCnpj(cnpj)).thenReturn(Optional.of(companyEntity));

        assertThrows(CompanyAlreadyExistsException.class,
                () -> springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser));

        verifyNoMoreInteractions(comparatorService, companyRepository);
        verifyNoInteractions(domainMapper, adapterMapper, cacheGateway);
    }
}