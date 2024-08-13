package br.com.devtt.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "city", schema = "info")
@EqualsAndHashCode(of = "id")
public class CityEntity {
    @Id private Integer id;
    private String name;
    @Column(name = "\"stateAcronym\"") private String stateAcronym;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"idState\"")
    private StateEntity state;
}