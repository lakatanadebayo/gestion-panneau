package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaysRepository extends JpaRepository<Pays, UUID> {
    List<Pays> findAllByOrderByNomAsc();
    Optional<Pays> findByNom(String nom);
}
