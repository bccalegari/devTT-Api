package br.com.devtt.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "state", schema = "info")
@EqualsAndHashCode(of = "id")
public class StateEntity {
    @Id private Integer id;
    private String name;
    @Column(name = "\"isoAlpha2\"") private String isoAlpha2;
}