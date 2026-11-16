package com.projet.gestion_panneau.dto;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private UUID id;
    private LocalDateTime dateLocation;
    private UUID idClient;
    private BigDecimal montantLocation;
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
    private List<LocationPanneauDto> listPanneaux;
}