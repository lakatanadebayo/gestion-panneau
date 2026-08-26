package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.Client;
import com.projet.gestion_panneau.entity.State;
import com.projet.gestion_panneau.repository.ClientRepository;
import com.projet.gestion_panneau.repository.PaysRepository;
import com.projet.gestion_panneau.repository.ClientRepository;
import com.projet.gestion_panneau.repository.RoleRepository;
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
@RequestMapping("/api/client")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final PaysRepository paysRepository;

    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    @PostMapping( "/save")
    public ResponseEntity<?> save(@Valid @RequestBody Client client) throws IOException {

        Optional<Client> optionalClient = clientRepository.findByUsername(client.getUsername());

        if(!optionalClient.isPresent()){
            Client tempClient = Client.builder()
                    .titre(client.getTitre())
                    .nom(client.getNom())
                    .prenom(client.getPrenom())
                    .dateNaissance(client.getDateNaissance())
                    .adresse(client.getAdresse())
                    .pays(paysRepository.findById(client.getPays().getId()).get())
                    .ville(client.getVille())
                    .codePostal(client.getCodePostal())
                    .email(client.getEmail())
                    .telephone(client.getTelephone())
                    .photo(client.getPhoto())
                    .username(client.getUsername())
                    .password(passwordEncoder.encode(client.getPassword()))
                    .state(State.INACTIVE.getState())
                    .isDeleted(false)
                    .roles(List.of(roleRepository.findByLibelle("ADMIN").get()))
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

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @PostMapping("/username")
    public ResponseEntity<?> findByUsernameAndPassword(@RequestBody LoginRequest loginRequest){
        Optional<Client> tempClient = clientRepository.findByUsername(loginRequest.getUsername());

        if(tempClient.isPresent()){
            if(passwordEncoder.matches(loginRequest.getPassword(), tempClient.get().getPassword())) {
                return new ResponseEntity<>(tempClient.get(), HttpStatus.OK);
            }else {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @PostMapping("/check-username")
    public ResponseEntity<Boolean> findByUsername(@RequestBody CheckByUsername checkByUsername){
        Optional<Client> tempClient = clientRepository.findByUsername(checkByUsername.getUsername());
        if(tempClient.isPresent()){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
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
