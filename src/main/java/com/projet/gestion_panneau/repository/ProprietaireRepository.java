package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, UUID> {
    @Query("""
                SELECT p
                FROM Proprietaire p
                WHERE p.user.id = :id
            """)
    Optional<Proprietaire> findByUserId(UUID id);
}