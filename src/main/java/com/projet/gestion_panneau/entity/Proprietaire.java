package com.projet.gestion_panneau.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PROPRIETAIRE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Proprietaire extends User {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "proprietaire_panneau", joinColumns = { @JoinColumn(name = "id_proprietaire") }, inverseJoinColumns = {@JoinColumn(name = "id_panneau") })
    private List<Panneau> panneaux = new ArrayList<Panneau>();

}