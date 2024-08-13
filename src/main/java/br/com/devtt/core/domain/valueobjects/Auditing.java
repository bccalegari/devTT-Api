package br.com.devtt.core.domain.valueobjects;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Auditing {
    private Long createdBy;
    private LocalDateTime createdDt;
    private Long updatedBy;
    private LocalDateTime updatedDt;
    private Long deletedBy;
    private LocalDateTime deletedDt;
}