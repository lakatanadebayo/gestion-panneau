package com.projet.gestion_panneau.entity;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import com.projet.gestion_panneau.audit.CreationAuditable;
import com.projet.gestion_panneau.audit.ModificationAuditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "proprietaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Proprietaire implements Serializable, CreationAuditable, ModificationAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @Embedded
    private AuditCreation auditCreation;

    private String codeUtilisateurCreation;

    @Embedded
    private AuditModification auditModification;

    private String codeUtilisateurModification;

    private String state;

    private Boolean isDeleted;

}