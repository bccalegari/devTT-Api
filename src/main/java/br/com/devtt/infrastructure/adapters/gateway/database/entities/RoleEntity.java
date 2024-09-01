package br.com.devtt.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "role", schema = "security")
@EqualsAndHashCode(of = "id")
public class RoleEntity {
    @Id
    private Integer id;

    private String name;
}