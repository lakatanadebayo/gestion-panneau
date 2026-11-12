package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.configuration.CustomUserDetailsService;
import com.projet.gestion_panneau.configuration.JwtService;
import com.projet.gestion_panneau.util.LoginRequest;
import com.projet.gestion_panneau.util.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            // 1. Vérification des identifiants
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // 2. Chargement des informations utilisateur
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());

            // 3. Génération du JWT
            String token = jwtService.generateToken(userDetails);

            // 4. Récupération des autorités
            List<String> authorities = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // 5. Construction de la réponse
            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .username(userDetails.getUsername())
                    .authorities(authorities)
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", 401, "message", "Nom d'utilisateur ou mot de passe incorrect"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", 401, "message", "Échec de l'authentification"));
        }
    }

}