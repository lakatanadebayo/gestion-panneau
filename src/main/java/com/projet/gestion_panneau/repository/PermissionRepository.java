package com.projet.gestion_panneau.repository;

import com.projet.gestion_panneau.entity.Permission;
import com.projet.gestion_panneau.enumeration.ActionEnum;
import com.projet.gestion_panneau.enumeration.ResourceEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByResourceAndAction(ResourceEnum resource, ActionEnum action);
}
