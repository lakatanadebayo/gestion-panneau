package com.projet.gestion_panneau.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends User {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_location", joinColumns = { @JoinColumn(name = "id_client") }, inverseJoinColumns = {@JoinColumn(name = "id_location") })
    private List<Location> locations = new ArrayList<Location>();

}