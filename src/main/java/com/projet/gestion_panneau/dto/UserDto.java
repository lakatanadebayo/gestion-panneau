package com.projet.gestion_panneau.dto;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.audit.AuditModification;
import com.projet.gestion_panneau.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String titre;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String adresse;
    private UUID idPays;
    private String ville;
    private String codePostal;
    private String email;
    private String telephone;
    private String username;
    private String photo;
    private String password;
    private String state;
    private List<Role> roles = new ArrayList<Role>();
    private Boolean isDeleted;
}
