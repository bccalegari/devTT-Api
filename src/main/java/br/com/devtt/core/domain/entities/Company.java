package br.com.devtt.core.domain.entities;

import br.com.devtt.core.domain.valueobjects.Auditing;
import br.com.devtt.core.domain.valueobjects.Cnpj;
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