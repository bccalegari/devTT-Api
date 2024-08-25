package br.com.devtt.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "company", schema = "client")
@EqualsAndHashCode(of = "id")
public class CompanyEntity {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<UserEntity> employees;
}