package com.projet.gestion_panneau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaysDto {
    private String nom;
    private String code;
    private String isoCode;
}
