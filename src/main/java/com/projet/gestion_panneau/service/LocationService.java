package com.projet.gestion_panneau.service;

import com.projet.gestion_panneau.entity.Location;
import com.projet.gestion_panneau.entity.LocationPanneau;
import com.projet.gestion_panneau.entity.Panneau;
import com.projet.gestion_panneau.repository.LocationRepository;
import com.projet.gestion_panneau.repository.PanneauRepository;
import com.projet.gestion_panneau.util.LocationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;
    private final PanneauRepository panneauRepository;
    private final PanneauService panneauService;

    public Location save(LocationRequest request) {

        Location location = Location.builder()
                .dateLocation(request.getDateLocation())
                .montantLocation(request.getMontantLocation())
                .isDeleted(false)
                .build();

        request.getLocationPanneaux().forEach(detailRequest -> {

            Panneau panneau = panneauRepository.findById(detailRequest.getPanneauId()).orElseThrow(() -> new RuntimeException("Panneau introuvable : " + detailRequest.getPanneauId()));

            if (!panneauService.estPanneauDisponible(panneau.getId(), detailRequest.getDateDebut(), detailRequest.getDateFin())) {

                throw new RuntimeException("Le panneau " + panneau.getId() + " est déjà loué pour cette période.");
            }

            LocationPanneau detail = LocationPanneau.builder()
                    .panneau(panneau)
                    .dateDebut(detailRequest.getDateDebut())
                    .dateFin(detailRequest.getDateFin())
                    .montantLigne(detailRequest.getMontantLigne())
                    .isDeleted(false)
                    .location(location)
                    .build();

            location.addLocationPanneau(detail);
        });

        return locationRepository.save(location);
    }

    public List<Location> getAll() {
        return locationRepository.findAll()
                .stream()
                .filter(location -> !Boolean.TRUE.equals(location.getIsDeleted())).toList();
    }

    public Optional<Location> getById(UUID id) {
        return locationRepository.findById(id).filter(location -> !Boolean.TRUE.equals(location.getIsDeleted()));
    }

    public boolean delete(UUID id) {

        Optional<Location> optionalLocation = locationRepository.findById(id);

        if (optionalLocation.isEmpty()) {
            return false;
        }

        Location location = optionalLocation.get();

        if (Boolean.TRUE.equals(location.getIsDeleted())) {
            return false;
        }

        location.setIsDeleted(true);

        if (location.getLocationPanneaux() != null) {
            location.getLocationPanneaux().forEach(detail -> detail.setIsDeleted(true));
        }

        locationRepository.save(location);

        return true;
    }
}