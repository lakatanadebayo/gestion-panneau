package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.dto.PaysDTO;
import com.projet.gestion_panneau.entity.Pays;
import com.projet.gestion_panneau.repository.PaysRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pays")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaysController {

    private final PaysRepository paysRepository;

    @PreAuthorize("hasAuthority('PAYS_WRITE')")
    @PostMapping( "/save")
    public ResponseEntity<?> saveOne(@Valid @RequestBody Pays pays) throws IOException {
        return new ResponseEntity<>(paysRepository.save(pays), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PAYS_WRITE')")
    @PostMapping("/save/all")
    public ResponseEntity<?> saveAll(@Valid @RequestBody List<PaysDTO> dtoList) {

        List<Pays> pays = dtoList.stream().map(dto ->
                Pays.builder()
                        .nom(dto.getNom())
                        .code(dto.getCode())
                        .isoCode(dto.getIsoCode())
                        .isDeleted(false)
                        .codeUtilisateurCreation("SYSTEM")
                        .build()
        ).toList();

        return ResponseEntity.ok(paysRepository.saveAll(pays));
    }

    @PreAuthorize("hasAuthority('PAYS_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<Pays> paysList = paysRepository.findAll();
        return new ResponseEntity<>(paysList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PAYS_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        Optional<Pays> pays = paysRepository.findById(id);
        if(pays.isPresent()){
            return new ResponseEntity<>(pays.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('PAYS_DELETE')")
    @DeleteMapping("/delele/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")UUID id){
        Optional<Pays> pays = paysRepository.findById(id);
        if(pays.isPresent()){
            pays.get().setIsDeleted(true);
            paysRepository.save(pays.get());
            return new ResponseEntity<>("Pays :"+id+" supprimé avec succès", HttpStatus.OK);
        }
        return new ResponseEntity<>("Echec suppression pays : "+id, HttpStatus.OK);
    }
}
