package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.LocationPanneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LocationPanneauRepository extends JpaRepository<LocationPanneau, UUID> {
    @Query("""
                SELECT COUNT(lp) > 0
                FROM LocationPanneau lp
                WHERE lp.panneau.id = :panneauId
                AND :dateDebut < lp.dateFin
                AND :dateFin > lp.dateDebut
            """)
    boolean estPanneauDisponible(@Param("panneauId") UUID panneauId, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

}
