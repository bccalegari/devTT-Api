package br.com.devtt.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "role", schema = "security")
public class RoleEntity {
    @Id
    @Column(name = "\"idRole\"")
    private Integer idRole;

    private String name;
}