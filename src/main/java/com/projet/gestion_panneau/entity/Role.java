package com.projet.gestion_panneau.entity;

import com.projet.gestion_panneau.audit.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners({AuditCreationListener.class, AuditModificationListener.class})
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldNameConstants
@EqualsAndHashCode
public class Role implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String libelle;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "id_role"), inverseJoinColumns = @JoinColumn(name = "id_permission"))
    private List<Permission> permissions = new ArrayList<>();
    @Embedded
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    @Embedded
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}
