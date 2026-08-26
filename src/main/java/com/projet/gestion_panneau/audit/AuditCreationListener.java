package com.projet.gestion_panneau.audit;

import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditCreationListener {

    @PrePersist
    public void setCreatedAt(CreationAuditable creationAuditable) {

        AuditCreation auditCreation = creationAuditable.getAuditCreation();

        if(auditCreation == null) {
            auditCreation = AuditCreation.builder().build();
            creationAuditable.setAuditCreation(auditCreation);
        }

        auditCreation.setDateCreation(LocalDateTime.now());

    }
}
