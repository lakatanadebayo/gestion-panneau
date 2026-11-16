package com.projet.gestion_panneau.service;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.dto.LocationDto;
import com.projet.gestion_panneau.entity.Client;
import com.projet.gestion_panneau.entity.Location;
import com.projet.gestion_panneau.entity.LocationPanneau;
import com.projet.gestion_panneau.entity.Panneau;
import com.projet.gestion_panneau.repository.ClientRepository;
import com.projet.gestion_panneau.repository.LocationPanneauRepository;
import com.projet.gestion_panneau.repository.LocationRepository;
import com.projet.gestion_panneau.repository.PanneauRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final ClientRepository clientRepository;
    private final LocationRepository locationRepository;
    private final LocationPanneauRepository locationPanneauRepository;
    private final PanneauRepository panneauRepository;
    private final PanneauService panneauService;

    public Location save(LocationDto locationDto) {

        Client client = clientRepository.findById(locationDto.getIdClient()).orElseThrow(() -> new RuntimeException("Client introuvable : " + locationDto.getIdClient()));

        Location location = Location.builder()
                .dateLocation(locationDto.getDateLocation())
                .client(client)
                .montantLocation(locationDto.getMontantLocation())
                .auditCreation(new AuditCreation(LocalDateTime.now()))
                .codeUtilisateurCreation("SYSTEM")
                .isDeleted(false)
                .build();
        Location savedLocation = locationRepository.save(location);

        locationDto.getListPanneaux().forEach(detailRequest -> {

            Panneau panneau = panneauRepository.findById(detailRequest.getIdPanneau()).orElseThrow(() -> new RuntimeException("Panneau introuvable : " + detailRequest.getIdPanneau()));

            if (!panneauService.estPanneauDisponible(panneau.getId(), detailRequest.getDateDebut(), detailRequest.getDateFin())) {
                throw new RuntimeException("Le panneau " + panneau.getId() + " est déjà loué pour cette période.");
            }

            LocationPanneau detail = LocationPanneau.builder()
                    .location(savedLocation)
                    .panneau(panneau)
                    .dateDebut(detailRequest.getDateDebut())
                    .dateFin(detailRequest.getDateFin())
                    .montantLigne(detailRequest.getMontantLigne())
                    .auditCreation(new AuditCreation(LocalDateTime.now()))
                    .codeUtilisateurCreation("SYSTEM")
                    .isDeleted(false)
                    .build();

            locationPanneauRepository.save(detail);
        });

        return savedLocation;
    }

    public List<Location> getAll() {
        return locationRepository.findAll()
                .stream()
                .filter(location -> !Boolean.TRUE.equals(location.getIsDeleted())).toList();
    }

    public Optional<Location> getById(UUID id) {
        return locationRepository.findById(id).filter(location -> !Boolean.TRUE.equals(location.getIsDeleted()));
    }

    public void delete(UUID idLocation) {

        Location location = locationRepository.findById(idLocation).orElseThrow(() -> new RuntimeException("Location panneau introuvable : " + idLocation));
        location.setIsDeleted(true);
        locationRepository.save(location);

        location.getLocationPanneaux().forEach(locationPanneau -> {

            LocationPanneau locationPanneauTemp = locationPanneauRepository.
                    findByLocationIdAndPanneauId(locationPanneau.getLocation().getId(), locationPanneau.getPanneau().getId())
                    .orElseThrow(() -> new RuntimeException("Detail location introuvable : " + locationPanneau.getLocation().getId()+", "+locationPanneau.getPanneau().getId()));
            locationPanneauTemp.setIsDeleted(true);
            locationPanneauRepository.save(locationPanneauTemp);
        });
    }
}