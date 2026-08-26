package com.projet.gestion_panneau.audit;

import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class AuditModificationListener {

    @PreUpdate
    public void setCreatedAt(ModificationAuditable modificationAuditable) {

        AuditModification auditModification = modificationAuditable.getAuditModification();

        if(auditModification == null) {
            auditModification = AuditModification.builder().build();
            modificationAuditable.setAuditModification(auditModification);
        }

        auditModification.setDateModification(LocalDateTime.now());

    }
}
