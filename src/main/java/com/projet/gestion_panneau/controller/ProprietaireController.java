package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.State;
import com.projet.gestion_panneau.entity.Proprietaire;
import com.projet.gestion_panneau.repository.PaysRepository;
import com.projet.gestion_panneau.repository.RoleRepository;
import com.projet.gestion_panneau.repository.ProprietaireRepository;
import com.projet.gestion_panneau.util.CheckByUsername;
import com.projet.gestion_panneau.util.LoginRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/proprietaire")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProprietaireController {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final RoleRepository roleRepository;
    private final ProprietaireRepository proprietaireRepository;
    private final PaysRepository paysRepository;

    @PreAuthorize("hasAuthority('PROPRIETAIRE_WRITE')")
    @PostMapping( "/save")
    public ResponseEntity<?> save(@Valid @RequestBody Proprietaire proprietaire) throws IOException {

        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findByUsername(proprietaire.getUsername());

        if(!optionalProprietaire.isPresent()){
            Proprietaire tempProprietaire = Proprietaire.builder()
                    .titre(proprietaire.getTitre())
                    .nom(proprietaire.getNom())
                    .prenom(proprietaire.getPrenom())
                    .dateNaissance(proprietaire.getDateNaissance())
                    .adresse(proprietaire.getAdresse())
                    .pays(paysRepository.findById(proprietaire.getPays().getId()).get())
                    .ville(proprietaire.getVille())
                    .codePostal(proprietaire.getCodePostal())
                    .email(proprietaire.getEmail())
                    .telephone(proprietaire.getTelephone())
                    .photo(proprietaire.getPhoto())
                    .username(proprietaire.getUsername())
                    .password(passwordEncoder.encode(proprietaire.getPassword()))
                    .state(State.INACTIVE.getState())
                    .isDeleted(false)
                    .roles(List.of(roleRepository.findByLibelle("ADMIN").get()))
                    .build();
            Proprietaire savedProprietaire = proprietaireRepository.save(tempProprietaire);
            return new ResponseEntity<>(savedProprietaire, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('PROPRIETAIRE_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<Proprietaire> proprietaires = proprietaireRepository.findAll()
                .stream()
                .filter(proprietaire -> !proprietaire.getIsDeleted())
                .toList();
        return ResponseEntity.ok(proprietaires);
    }

    @PreAuthorize("hasAuthority('PROPRIETAIRE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        Optional<Proprietaire> proprietaire = proprietaireRepository.findById(id);
        if(proprietaire.isPresent()){
            return new ResponseEntity<>(proprietaire.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('PROPRIETAIRE_READ')")
    @PostMapping("/username")
    public ResponseEntity<?> findByUsernameAndPassword(@RequestBody LoginRequest loginRequest){
        Optional<Proprietaire> tempProprietaire = proprietaireRepository.findByUsername(loginRequest.getUsername());

        if(tempProprietaire.isPresent()){
            if(passwordEncoder.matches(loginRequest.getPassword(), tempProprietaire.get().getPassword())) {
                return new ResponseEntity<>(tempProprietaire.get(), HttpStatus.OK);
            }else {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('PROPRIETAIRE_READ')")
    @PostMapping("/check-username")
    public ResponseEntity<Boolean> findByUsername(@RequestBody CheckByUsername checkByUsername){
        Optional<Proprietaire> tempProprietaire = proprietaireRepository.findByUsername(checkByUsername.getUsername());
        if(tempProprietaire.isPresent()){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('PROPRIETAIRE_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id")UUID id){
        Optional<Proprietaire> proprietaire = proprietaireRepository.findById(id);
        if(proprietaire.isPresent()){
            proprietaire.get().setIsDeleted(true);
            proprietaireRepository.save(proprietaire.get());
            return new ResponseEntity<>("Proprietaire :"+id+" supprimé avec succès", HttpStatus.OK);
        }
        return new ResponseEntity<>("Echec suppression proprietaire : "+id, HttpStatus.OK);
    }
}
