package com.projet.gestion_panneau.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projet.gestion_panneau.audit.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@ToString(exclude = {"client", "locationPanneaux"})
@EqualsAndHashCode(exclude = {"client", "locationPanneaux"})
public class Location implements Serializable, CreationAuditable, ModificationAuditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime dateLocation;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    @NotNull(message = "Le client est obligatoire")
    private Client client;

    private BigDecimal montantLocation;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<LocationPanneau> locationPanneaux = new HashSet<>();

    @Embedded
    private AuditCreation auditCreation;

    private String codeUtilisateurCreation;

    @Embedded
    private AuditModification auditModification;

    private String codeUtilisateurModification;

    private Boolean isDeleted;

    public void addLocationPanneau(LocationPanneau locationPanneau) {
        if (locationPanneau != null) {
            locationPanneaux.add(locationPanneau);
            locationPanneau.setLocation(this);
        }
    }

    public void removeLocationPanneau(LocationPanneau locationPanneau) {
        if (locationPanneau != null) {
            locationPanneaux.remove(locationPanneau);
            locationPanneau.setLocation(null);
        }
    }
}