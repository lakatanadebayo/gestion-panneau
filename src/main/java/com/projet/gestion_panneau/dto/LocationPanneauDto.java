package com.projet.gestion_panneau.dto;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationPanneauDto {
    private UUID id;
    private UUID idLocation;
    private UUID idPanneau;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private BigDecimal montantLigne;
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}