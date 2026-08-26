package com.projet.gestion_panneau.service;

import com.projet.gestion_panneau.entity.Panneau;
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
public class PanneauService {

    private final PanneauRepository panneauRepository;

    public Panneau save(Panneau panneau) {
        return panneauRepository.save(panneau);
    }

    public List<Panneau> getAll() {
        return panneauRepository.findAll()
                .stream()
                .filter(location -> !Boolean.TRUE.equals(location.getIsDeleted())).toList();
    }

    public Optional<Panneau> getById(UUID id) {
        return panneauRepository.findById(id).filter(location -> !Boolean.TRUE.equals(location.getIsDeleted()));
    }

    public boolean delete(UUID id) {

        Optional<Panneau> optionalPanneau = panneauRepository.findById(id);

        if (optionalPanneau.isEmpty()) {
            return false;
        }

        Panneau panneau = optionalPanneau.get();

        if (Boolean.TRUE.equals(panneau.getIsDeleted())) {
            return false;
        }

        panneau.setIsDeleted(true);

        if (panneau.getLocationPanneaux() != null) {
            panneau.getLocationPanneaux().forEach(detail -> detail.setIsDeleted(true));
        }

        panneauRepository.save(panneau);

        return true;
    }

    public boolean estPanneauDisponible(UUID panneauId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return !panneauRepository.estPanneauDisponible(panneauId, dateFin, dateDebut);
    }

    public List<Panneau> getPanneauxDisponiblesSelonEmplacement(String emplacement, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return panneauRepository.panneauxDisponiblesSelonEmplacement(emplacement, dateDebut, dateFin);
    }
}