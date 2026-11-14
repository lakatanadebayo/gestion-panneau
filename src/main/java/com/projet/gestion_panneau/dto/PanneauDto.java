package com.projet.gestion_panneau.dto;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PanneauDto {
    private UUID id;
    private String emplacement;
    private UUID idProprietaire;
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}
