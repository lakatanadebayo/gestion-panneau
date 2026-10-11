package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Proprietaire;
import com.projet.gestion_panneau.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, UUID> {
    Optional<Proprietaire> findByUser(User user);
}