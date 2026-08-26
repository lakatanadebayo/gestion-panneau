package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.Location;
import com.projet.gestion_panneau.service.LocationService;
import com.projet.gestion_panneau.util.LocationRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/save")
    public ResponseEntity<Location> save(@Valid @RequestBody LocationRequest request) {
        return ResponseEntity.ok(locationService.save(request));
    }


    @GetMapping("/all")
    public ResponseEntity<List<Location>> getAll() {
        return ResponseEntity.ok(locationService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable UUID id) {
        return locationService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        if (!locationService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
