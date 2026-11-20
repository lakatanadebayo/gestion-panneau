package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query("""
                SELECT c
                FROM Client c
                WHERE c.user.id = :id
            """)
    Optional<Client> findByUserId(UUID id);
}