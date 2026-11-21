package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.dto.PanneauDto;
import com.projet.gestion_panneau.entity.Panneau;
import com.projet.gestion_panneau.entity.Pays;
import com.projet.gestion_panneau.entity.Proprietaire;
import com.projet.gestion_panneau.repository.PaysRepository;
import com.projet.gestion_panneau.repository.ProprietaireRepository;
import com.projet.gestion_panneau.service.PanneauService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/panneau")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PanneauController {

    private final PanneauService panneauService;
    private final PaysRepository paysRepository;
    private final ProprietaireRepository proprietaireRepository;

    @PreAuthorize("hasAuthority('PANNEAU_WRITE')")
    @PostMapping("/save")
    public ResponseEntity<Panneau> save(@Valid @RequestBody PanneauDto panneauDto) {

        Pays pays = paysRepository.findById(panneauDto.getIdPays())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Le pays spécifié est introuvable."
                ));

        Proprietaire proprietaire = proprietaireRepository.findById(panneauDto.getIdProprietaire())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Propriétaire introuvable"));

        Panneau panneau = Panneau.builder()
                .adresse(panneauDto.getAdresse())
                .ville(panneauDto.getVille())
                .pays(pays)
                .proprietaire(proprietaire)
                .auditCreation(new AuditCreation(LocalDateTime.now()))
                .codeUtilisateurCreation("SYSTEM")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(panneauService.save(panneau));
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping("/all")
    public ResponseEntity<List<Panneau>> getAll() {
        return ResponseEntity.ok(panneauService.getAll());
    }

    @PreAuthorize("hasAuthority('PANNEAU_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<Panneau> getById(@PathVariable UUID id) {
        return panneauService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('PANNEAU_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        if (!panneauService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
