package br.com.devtt.core.company.application.strategies;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpringGetCompanyCnpjFilterStrategyUnitTest {
    @InjectMocks
    private SpringGetCompanyCnpjFilterStrategy springGetCompanyCnpjFilterStrategy;
    @Mock
    private CompanyRepository<CompanyEntity> companyRepository;

    @Test
    void shouldGetAllCnpjs() {
        springGetCompanyCnpjFilterStrategy.execute();
        verify(companyRepository).getAllCnpjs();
    }
}