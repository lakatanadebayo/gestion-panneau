package com.projet.gestion_panneau.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import com.projet.gestion_panneau.audit.CreationAuditable;
import com.projet.gestion_panneau.audit.ModificationAuditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_user", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"pays", "roles"})
@FieldNameConstants
@EqualsAndHashCode(exclude = {"pays", "roles"})
public abstract class User implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String titre;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String adresse;
    @ManyToOne
    @JoinColumn(name = "id_pays", nullable = false)
    @NotNull(message = "Le pays est obligatoire")
    private Pays pays;
    private String ville;
    private String codePostal;
    private String email;
    private String telephone;
    private String username;
    private String photo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String state = State.INACTIVE.getState();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "id_user") }, inverseJoinColumns = {@JoinColumn(name = "id_role") })
    private List<Role> roles = new ArrayList<Role>();
    @Embedded
    private AuditCreation auditCreation;
    private String codeUtilisateurCreation;
    @Embedded
    private AuditModification auditModification;
    private String codeUtilisateurModification;
    private Boolean isDeleted;
}
