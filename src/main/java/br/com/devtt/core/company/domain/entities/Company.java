package br.com.devtt.core.company.domain.entities;

import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Company {
    private Integer id;
    private String name;
    private Cnpj cnpj;
    private Auditing auditing;
}