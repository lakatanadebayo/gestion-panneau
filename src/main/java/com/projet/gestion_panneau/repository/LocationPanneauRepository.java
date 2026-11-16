package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.LocationPanneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LocationPanneauRepository extends JpaRepository<LocationPanneau, UUID> {
    @Query("""
                SELECT lp
                FROM LocationPanneau lp
                WHERE lp.location.id = :idLocation
                  AND lp.panneau.id = :idPanneau
                  AND lp.isDeleted = false
            """)
    Optional<LocationPanneau> findByLocationIdAndPanneauId(@Param("idLocation") UUID idLocation, @Param("idPanneau") UUID idPanneau);
}
