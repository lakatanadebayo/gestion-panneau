package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.Location;
import com.projet.gestion_panneau.entity.LocationPanneau;
import com.projet.gestion_panneau.entity.Panneau;
import com.projet.gestion_panneau.repository.LocationRepository;
import com.projet.gestion_panneau.repository.PanneauRepository;
import com.projet.gestion_panneau.util.LocationPanneauRequest;
import com.projet.gestion_panneau.util.LocationRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LocationController {

    private final LocationRepository locationRepository;
    private final PanneauRepository panneauRepository;

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<Location> save(@Valid @RequestBody LocationRequest request) {

        Location location = Location.builder()
                .dateLocation(request.getDateLocation())
                .montantLocation(request.getMontantLocation())
                .isDeleted(false)
                .build();

        if (request.getLocationPanneaux() != null) {

            for (LocationPanneauRequest detailRequest : request.getLocationPanneaux()) {

                Panneau panneau = panneauRepository.findById(detailRequest.getPanneauId()).orElseThrow(() -> new RuntimeException("Panneau introuvable : " + detailRequest.getPanneauId()));

                LocationPanneau detail = LocationPanneau.builder()
                        .panneau(panneau)
                        .dateDebut(detailRequest.getDateDebut())
                        .dateFin(detailRequest.getDateFin())
                        .montantLigne(detailRequest.getMontantLigne())
                        .isDeleted(false)
                        .build();

                location.addLocationPanneau(detail);
            }
        }

        Location savedLocation = locationRepository.save(location);

        return ResponseEntity.ok(savedLocation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Location>> getAll() {

        List<Location> locations = locationRepository.findAll()
                .stream()
                .filter(location -> !Boolean.TRUE.equals(location.getIsDeleted()))
                .toList();

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable UUID id) {

        Optional<Location> optionalLocation = locationRepository.findById(id);

        if (optionalLocation.isEmpty() || Boolean.TRUE.equals(optionalLocation.get().getIsDeleted())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalLocation.get());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        Optional<Location> optionalLocation = locationRepository.findById(id);

        if (optionalLocation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Location location = optionalLocation.get();

        if (Boolean.TRUE.equals(location.getIsDeleted())) {
            return ResponseEntity.notFound().build();
        }

        location.setIsDeleted(true);

        if (location.getLocationPanneaux() != null) {
            location.getLocationPanneaux()
                    .forEach(detail -> detail.setIsDeleted(true));
        }

        locationRepository.save(location);

        return ResponseEntity.noContent().build();
    }
}
