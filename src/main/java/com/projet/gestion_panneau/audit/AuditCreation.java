package com.projet.gestion_panneau.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode
public class AuditCreation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "tms_creation", updatable = false, nullable = false)
    private LocalDateTime dateCreation;
}
