package br.com.devtt.core.domain.valueobjects;

import br.com.devtt.core.domain.entities.City;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Address {
    private String street;
    private Integer streetNumber;
    private String district;
    private String complement;
    private Cep cep;
    private City city;
}