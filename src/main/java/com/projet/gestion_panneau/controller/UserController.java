package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.entity.State;
import com.projet.gestion_panneau.entity.User;
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

import java.io.IOException;
import java.util.List;
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

    @PreAuthorize("hasAuthority('USER_WRITE')")
    @PostMapping( "/save")
    public ResponseEntity<?> save(@Valid @RequestBody User user) throws IOException {

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if(!optionalUser.isPresent()){
            User tempUser = User.builder()
                    .titre(user.getTitre())
                    .nom(user.getNom())
                    .prenom(user.getPrenom())
                    .dateNaissance(user.getDateNaissance())
                    .adresse(user.getAdresse())
                    .pays(paysRepository.findById(user.getPays().getId()).get())
                    .ville(user.getVille())
                    .codePostal(user.getCodePostal())
                    .email(user.getEmail())
                    .telephone(user.getTelephone())
                    .photo(user.getPhoto())
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .state(State.INACTIVE.getState())
                    .isDeleted(false)
                    .roles(List.of(roleRepository.findByLibelle("ADMIN").get()))
                    .build();
            User savedUser = userRepository.save(tempUser);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> !user.getIsDeleted())
                .toList();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @PostMapping("/authentication")
    public ResponseEntity<?> findByUsernameAndPassword(@RequestBody LoginRequest loginRequest){
        Optional<User> tempUser = userRepository.findByUsername(loginRequest.getUsername());

        if(tempUser.isPresent()){
            if(passwordEncoder.matches(loginRequest.getPassword(), tempUser.get().getPassword())) {
                return new ResponseEntity<>(tempUser.get(), HttpStatus.OK);
            }else {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @PostMapping("/check-username")
    public ResponseEntity<Boolean> findByUsername(@RequestBody CheckByUsername checkByUsername){
        Optional<User> tempUser = userRepository.findByUsername(checkByUsername.getUsername());
        if(tempUser.isPresent()){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setIsDeleted(true);
            userRepository.save(user.get());
            return new ResponseEntity<>("User :"+id+" supprimé avec succès", HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
