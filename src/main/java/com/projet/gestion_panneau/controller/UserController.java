package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.audit.AuditCreation;
import com.projet.gestion_panneau.dto.UserDto;
import com.projet.gestion_panneau.entity.*;
import com.projet.gestion_panneau.repository.PaysRepository;
import com.projet.gestion_panneau.repository.RoleRepository;
import com.projet.gestion_panneau.repository.UserRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final PaysRepository paysRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody UserDto userDto) throws IOException {

        // Vérifier si le username existe déjà
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ce nom d'utilisateur existe déjà.");
        }

        // Récupérer le pays
        Pays pays = paysRepository.findById(userDto.getIdPays())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Le pays spécifié est introuvable."
                ));

        // Récupérer le rôle
        Role role = roleRepository.findByLibelle("ADMIN")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Le rôle ADMIN n'est pas configuré."
                ));

        // Construire l'utilisateur
        User user = userDtoToUser(userDto, pays);

        user.setState(State.ACTIVE.getState());
        user.setIsDeleted(false);
        user.setRoles(List.of(role));
        user.setAuditCreation(new AuditCreation(LocalDateTime.now()));
        user.setCodeUtilisateurCreation("SYSTEM");

        // Enregistrer
        User savedUser = userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> !user.getIsDeleted())
                .toList();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {

        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @PostMapping("/authentication")
    public ResponseEntity<?> findByUsernameAndPassword(@Valid @RequestBody LoginRequest loginRequest) {

        // Recherche de l'utilisateur
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername().trim());

        // Vérification des identifiants
        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Identifiant ou mot de passe incorrect"));
        }

        User user = optionalUser.get();

        // Vérification du mot de passe
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Identifiant ou mot de passe incorrect"));
        }

        // Vérification de l'état du compte
        if ("Inactive".equals(user.getState())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("status", 403, "message", "Votre compte est désactivé"));
        }

        // Retourner l'entité User
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @PostMapping("/check-username")
    public ResponseEntity<Boolean> findByUsername(@RequestBody CheckByUsername checkByUsername) {
        Optional<User> tempUser = userRepository.findByUsername(checkByUsername.getUsername());
        if (tempUser.isPresent()) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setIsDeleted(true);
                    userRepository.save(user);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private User userDtoToUser(UserDto userDto, Pays pays) {

        return User.builder()
                .titre(userDto.getTitre())
                .nom(userDto.getNom())
                .prenom(userDto.getPrenom())
                .dateNaissance(userDto.getDateNaissance())
                .adresse(userDto.getAdresse())
                .pays(pays)
                .ville(userDto.getVille())
                .codePostal(userDto.getCodePostal())
                .email(userDto.getEmail())
                .telephone(userDto.getTelephone())
                .username(userDto.getUsername())
                .photo(userDto.getPhoto())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .state(userDto.getState())
                .roles(userDto.getRoles())
                .isDeleted(userDto.getIsDeleted())
                .build();
    }
}
