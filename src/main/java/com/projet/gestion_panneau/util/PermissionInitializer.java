package com.projet.gestion_panneau.util;

import com.projet.gestion_panneau.entity.Permission;
import com.projet.gestion_panneau.enumeration.ActionEnum;
import com.projet.gestion_panneau.enumeration.ResourceEnum;
import com.projet.gestion_panneau.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        createPermission(ResourceEnum.LOCATION, ActionEnum.READ, "Lecture des locations de panneau");
        createPermission(ResourceEnum.LOCATION, ActionEnum.WRITE, "Création ou modification des locations de panneau");
        createPermission(ResourceEnum.LOCATION, ActionEnum.DELETE, "Suppression des locations de panneau");

        createPermission(ResourceEnum.LOCATION_PANNEAU, ActionEnum.READ, "Lecture des détails de locations de panneau");
        createPermission(ResourceEnum.LOCATION_PANNEAU, ActionEnum.WRITE, "Création ou modification des détails de locations de panneau");
        createPermission(ResourceEnum.LOCATION_PANNEAU, ActionEnum.DELETE, "Suppression des détails de locations de panneau");

        createPermission(ResourceEnum.PANNEAU, ActionEnum.READ, "Lecture des panneaux");
        createPermission(ResourceEnum.PANNEAU, ActionEnum.WRITE, "Création ou modification des panneaux");
        createPermission(ResourceEnum.PANNEAU, ActionEnum.DELETE, "Suppression des panneaux");

        createPermission(ResourceEnum.PAYS, ActionEnum.READ, "Lecture des pays");
        createPermission(ResourceEnum.PAYS, ActionEnum.WRITE, "Création ou modification des pays");
        createPermission(ResourceEnum.PAYS, ActionEnum.DELETE, "Suppression des pays");

        createPermission(ResourceEnum.PROPRIETAIRE, ActionEnum.READ, "Lecture des propriétaires de panneau");
        createPermission(ResourceEnum.PROPRIETAIRE, ActionEnum.WRITE, "Création ou modification des propriétaires de panneau");
        createPermission(ResourceEnum.PROPRIETAIRE, ActionEnum.DELETE, "Suppression des propriétaires de panneau");

        createPermission(ResourceEnum.ROLE, ActionEnum.READ, "Lecture des rôles");
        createPermission(ResourceEnum.ROLE, ActionEnum.WRITE, "Création ou modification des rôles");
        createPermission(ResourceEnum.ROLE, ActionEnum.DELETE, "Suppression des rôles");
    }
    private void createPermission(ResourceEnum resource, ActionEnum action, String libelle) {
        if (permissionRepository.findByResourceAndAction(resource, action).isEmpty()) {
            Permission permission = Permission.builder()
                    .resource(resource)
                    .action(action)
                    .libelle(libelle)
                    .isDeleted(false)
                    .build();
            permissionRepository.save(permission);
        }
    }
}