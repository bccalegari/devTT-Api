package br.com.devtt.infrastructure.adapters.gateway.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company", schema = "client")
@EqualsAndHashCode(of = "id")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String cnpj;

    @Column(name = "\"createdBy\"")
    private Long createdBy;

    @Column(name = "\"createdDt\"")
    @Builder.Default
    private LocalDateTime createdDt = LocalDateTime.now();

    @Column(name = "\"updatedBy\"")
    private Long updatedBy;

    @Column(name = "\"updatedDt\"")
    private LocalDateTime updatedDt;

    @Column(name = "\"deletedBy\"")
    private Long deletedBy;

    @Column(name = "\"deletedDt\"")
    private LocalDateTime deletedDt;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<UserEntity> employees;
}