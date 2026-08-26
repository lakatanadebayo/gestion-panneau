package com.projet.gestion_panneau.entity;

import com.projet.gestion_panneau.audit.*;
import com.projet.gestion_panneau.enumeration.ActionEnum;
import com.projet.gestion_panneau.enumeration.ResourceEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.UUID;

@Entity
@EntityListeners({AuditCreationListener.class, AuditModificationListener.class})
@Table(
        name = "permission",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permission_resource_action",
                        columnNames = {"resource", "action"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldNameConstants
@EqualsAndHashCode
public class Permission implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ResourceEnum resource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ActionEnum action;

    @Column(length = 255)
    private String libelle;
    @Embedded
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    @Embedded
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}
