package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
