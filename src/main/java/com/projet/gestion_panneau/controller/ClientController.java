package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.Client;
import com.projet.gestion_panneau.repository.ClientRepository;
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
@RequestMapping("/api/client")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ClientRepository clientRepository;

    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    @PostMapping( "/save")
    public ResponseEntity<?> save(@Valid @RequestBody Client client) throws IOException {

        Optional<Client> optionalClient = clientRepository.findByUser(client.getUser());

        if(!optionalClient.isPresent()){
            Client tempClient = Client.builder()
                    .user(client.getUser())
                    .locations(client.getLocations())
                    .isDeleted(false)
                    .build();
            Client savedClient = clientRepository.save(tempClient);
            return new ResponseEntity<>(savedClient, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<Client> clients = clientRepository.findAll()
                .stream()
                .filter(client -> !client.getIsDeleted())
                .toList();
        return ResponseEntity.ok(clients);
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        Optional<Client> client = clientRepository.findById(id);
        if(client.isPresent()){
            return new ResponseEntity<>(client.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id")UUID id){
        Optional<Client> client = clientRepository.findById(id);
        if(client.isPresent()){
            client.get().setIsDeleted(true);
            clientRepository.save(client.get());
            return new ResponseEntity<>("Client :"+id+" supprimé avec succès", HttpStatus.OK);
        }
        return new ResponseEntity<>("Echec suppression client : "+id, HttpStatus.OK);
    }
}
