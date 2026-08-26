package com.projet.gestion_panneau.audit;

public interface ModificationAuditable {
    AuditModification  getAuditModification();
    void setAuditModification(AuditModification auditModification);
}
