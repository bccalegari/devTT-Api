package br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities;

import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.CityEntity;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "client")
@EqualsAndHashCode(of = "id")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "\"lastName\"")
    private String lastName;

    private Long phone;

    private String email;

    private String password;

    private String cpf;

    @Column(name = "\"birthDate\"")
    private LocalDate birthDate;

    private Character sex;

    private String street;

    @Column(name = "\"streetNumber\"")
    private Integer streetNumber;

    private String district;

    private String complement;

    private String cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"idCity\"")
    private CityEntity city;

    @Column(name = "\"createdBy\"")
    @Setter
    private Long createdBy;

    @Column(name = "\"createdDt\"")
    private LocalDateTime createdDt;

    @Column(name = "\"updatedBy\"")
    private Long updatedBy;

    @Column(name = "\"updatedDt\"")
    private LocalDateTime updatedDt;

    @Column(name = "\"deletedBy\"")
    @Setter
    private Long deletedBy;

    @Column(name = "\"deletedDt\"")
    private LocalDateTime deletedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"idRole\"")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"idCompany\"")
    private CompanyEntity company;

    @Column(name = "\"firstAccess\"")
    private Boolean firstAccess;
}