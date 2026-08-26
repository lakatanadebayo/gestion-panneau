package com.projet.gestion_panneau.entity;

import com.projet.gestion_panneau.audit.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.UUID;

@Entity
@EntityListeners({AuditCreationListener.class, AuditModificationListener.class})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldNameConstants
@EqualsAndHashCode
public class Pays implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nom;
    private String code;
    private String isoCode;
    @Embedded
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    @Embedded
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}
