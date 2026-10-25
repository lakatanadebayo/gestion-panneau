package com.projet.gestion_panneau.entity;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import com.projet.gestion_panneau.audit.CreationAuditable;
import com.projet.gestion_panneau.audit.ModificationAuditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Client implements Serializable, CreationAuditable, ModificationAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_location", joinColumns = { @JoinColumn(name = "id_client") }, inverseJoinColumns = {@JoinColumn(name = "id_location") })
    private List<Location> locations = new ArrayList<Location>();

    @Embedded
    private AuditCreation auditCreation;

    private String codeUtilisateurCreation;

    @Embedded
    private AuditModification auditModification;

    private String codeUtilisateurModification;

    private Boolean isDeleted;

}