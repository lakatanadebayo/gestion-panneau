package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.LocationPanneau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationPanneauRepository extends JpaRepository<LocationPanneau, UUID> {
}
