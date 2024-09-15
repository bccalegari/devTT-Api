package br.com.devtt.core.company.abstractions.application.strategies;

import java.util.List;

public interface GetCompanyFilterStrategy {
    List<String> execute();
}