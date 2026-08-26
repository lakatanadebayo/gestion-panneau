package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Panneau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PanneauRepository extends JpaRepository<Panneau, UUID> {
}
