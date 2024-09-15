package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.factories.GetCompanyFilterStrategyFactory;
import br.com.devtt.core.company.abstractions.application.strategies.GetCompanyFilterStrategy;
import br.com.devtt.core.company.domain.valueobjects.CompanyFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringGetCompanyFilterUseCaseUnitTest {
    @InjectMocks private SpringGetCompanyFilterUseCase springGetCompanyFilterUseCase;
    @Mock private GetCompanyFilterStrategyFactory getCompanyFilterStrategyFactory;
    @Mock private GetCompanyFilterStrategy getCompanyFilterStrategy;

    @Test
    void shouldGetCompanyFilter() {
        when(getCompanyFilterStrategyFactory.execute(any(CompanyFilter.class))).thenReturn(getCompanyFilterStrategy);
        when(getCompanyFilterStrategy.execute()).thenReturn(List.of("filter"));
        var companyFilterOutputDto = springGetCompanyFilterUseCase.execute("name");
        verify(getCompanyFilterStrategyFactory).execute(CompanyFilter.NAME);
        verify(getCompanyFilterStrategy).execute();
        assertNotNull(companyFilterOutputDto);
    }

    @Test
    void shouldGetEmptyCompanyFilter() {
        var companyFilterOutputDto = springGetCompanyFilterUseCase.execute("");
        verifyNoInteractions(getCompanyFilterStrategyFactory, getCompanyFilterStrategy);
        assertEquals(0, companyFilterOutputDto.data().size());
    }
}