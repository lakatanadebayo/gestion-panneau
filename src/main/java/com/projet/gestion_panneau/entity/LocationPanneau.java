package com.projet.gestion_panneau.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projet.gestion_panneau.audit.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners({AuditCreationListener.class, AuditModificationListener.class})
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_location_panneau", columnNames = {"id_location", "id_panneau"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
@ToString(exclude = {"location", "panneau"})
@EqualsAndHashCode(exclude = {"location", "panneau"})
public class LocationPanneau implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location", nullable = false)
    @JsonBackReference
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_panneau", nullable = false)
    @JsonBackReference
    private Panneau panneau;

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
    private LocalDateTime dateFin;

    private BigDecimal montantLigne;

    @Embedded
    private AuditCreation auditCreation;

    private String codeUtilisateurCreation;

    @Embedded
    private AuditModification auditModification;

    private String codeUtilisateurModification;

    private Boolean isDeleted;
}