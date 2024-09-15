package br.com.devtt.core.company.application.factories;

import br.com.devtt.core.company.application.strategies.SpringGetCompanyCnpjFilterStrategy;
import br.com.devtt.core.company.application.strategies.SpringGetCompanyNameFilterStrategy;
import br.com.devtt.core.company.domain.valueobjects.CompanyFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SpringGetCompanyFilterStrategyFactoryUnitTest {
    @InjectMocks private SpringGetCompanyFilterStrategyFactory springGetCompanyFilterStrategyFactory;
    @Mock private SpringGetCompanyNameFilterStrategy springGetCompanyNameFilterStrategy;
    @Mock private SpringGetCompanyCnpjFilterStrategy springGetCompanyCnpjFilterStrategy;

    @BeforeEach
    void setup() {
        springGetCompanyFilterStrategyFactory = new SpringGetCompanyFilterStrategyFactory(
            springGetCompanyNameFilterStrategy,
            springGetCompanyCnpjFilterStrategy
        );
    }

    @Test
    void shouldReturnGetCompanyNameFilterStrategy() {
        var companyFilter = CompanyFilter.NAME;
        var getCompanyFilterStrategy = springGetCompanyFilterStrategyFactory.execute(companyFilter);
        assertNotNull(getCompanyFilterStrategy);
        assertInstanceOf(SpringGetCompanyNameFilterStrategy.class, getCompanyFilterStrategy);
    }

    @Test
    void shouldReturnGetCompanyCnpjFilterStrategy() {
        var companyFilter = CompanyFilter.CNPJ;
        var getCompanyFilterStrategy = springGetCompanyFilterStrategyFactory.execute(companyFilter);
        assertNotNull(getCompanyFilterStrategy);
        assertInstanceOf(SpringGetCompanyCnpjFilterStrategy.class, getCompanyFilterStrategy);
    }
}