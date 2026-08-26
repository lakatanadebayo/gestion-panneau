package com.projet.gestion_panneau.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    private LocalDateTime dateLocation;
    private BigDecimal montantLocation;
    @Builder.Default
    private Set<LocationPanneauRequest> locationPanneaux = new HashSet<>();
}