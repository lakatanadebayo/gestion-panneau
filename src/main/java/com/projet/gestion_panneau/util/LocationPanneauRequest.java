package com.projet.gestion_panneau.util;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationPanneauRequest {
    private UUID panneauId;
    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private LocalDateTime dateDebut;
    @NotNull(message = "La date de fin est obligatoire")
    @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
    private LocalDateTime dateFin;
    private BigDecimal montantLigne;
}