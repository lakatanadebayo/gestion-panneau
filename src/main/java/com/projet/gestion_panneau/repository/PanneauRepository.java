package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Panneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PanneauRepository extends JpaRepository<Panneau, UUID> {
    @Query("""
                SELECT COUNT(lp) > 0
                FROM LocationPanneau lp
                WHERE lp.panneau.id = :panneauId
                AND :dateDebut < lp.dateFin
                AND :dateFin > lp.dateDebut
            """)
    boolean estPanneauDisponible(@Param("panneauId") UUID panneauId, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    @Query("""
                SELECT p
                FROM Panneau p
                WHERE ( :emplacement IS NULL OR :emplacement = '' OR p.emplacement = :emplacement )
                AND NOT EXISTS (
                    SELECT lp
                    FROM LocationPanneau lp
                    WHERE lp.panneau = p
                    AND lp.isDeleted = false
                    AND :dateDebut < lp.dateFin
                    AND :dateFin > lp.dateDebut
                )
                AND p.isDeleted = false
            """)
    List<Panneau> panneauxDisponiblesSelonEmplacement(@Param("emplacement") String emplacement, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);
}
