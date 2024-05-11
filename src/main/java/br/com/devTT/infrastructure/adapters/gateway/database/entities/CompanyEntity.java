package br.com.devTT.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "company", schema = "client")
public class CompanyEntity {
    @Id
    @Column(name = "\"idCompany\"")
    private Integer idCompany;

    private String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<UserEntity> employees;
}