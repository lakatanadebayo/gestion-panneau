package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.Role;
import com.projet.gestion_panneau.repository.RoleRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleRepository roleRepository;

    @PreAuthorize("hasAuthority('ROLE_WRITE')")
    @PostMapping("/save")
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        roleRepository.save(role);
        return new ResponseEntity<>(role, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<Role> roleList = roleRepository.findAll()
                .stream()
                .filter(role -> !role.getIsDeleted())
                .toList();
        return ResponseEntity.ok(roleList);
    }

    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent()){
            return new ResponseEntity<>(role.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping("/{libelle}")
    public ResponseEntity<?> findByLibelle(@PathVariable("libelle") String libelle){
        Optional<Role> role = roleRepository.findByLibelle(libelle);
        if(role.isPresent()){
            return new ResponseEntity<>(role.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}
