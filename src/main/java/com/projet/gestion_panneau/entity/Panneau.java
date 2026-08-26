package com.projet.gestion_panneau.entity;

import com.projet.gestion_panneau.audit.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners({AuditCreationListener.class, AuditModificationListener.class})
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
@ToString(exclude = "locationPanneaux")
@EqualsAndHashCode(exclude = "locationPanneaux")
public class Panneau implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToMany(mappedBy = "panneau")
    @Builder.Default
    private Set<LocationPanneau> locationPanneaux = new HashSet<>();
    @Embedded
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    @Embedded
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}
