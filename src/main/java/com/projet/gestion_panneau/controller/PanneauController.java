package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.Panneau;
import com.projet.gestion_panneau.service.PanneauService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/panneau")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PanneauController {

    private final PanneauService panneauService;

    @PostMapping("/save")
    public ResponseEntity<Panneau> save(@Valid @RequestBody Panneau panneau) {
        return ResponseEntity.ok(panneauService.save(panneau));
    }


    @GetMapping("/all")
    public ResponseEntity<List<Panneau>> getAll() {
        return ResponseEntity.ok(panneauService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Panneau> getById(@PathVariable UUID id) {
        return panneauService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        if (!panneauService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
