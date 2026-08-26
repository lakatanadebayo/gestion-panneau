package com.projet.gestion_panneau.controller;

import com.projet.gestion_panneau.configuration.CustomUserDetailsService;
import com.projet.gestion_panneau.configuration.JwtService;
import com.projet.gestion_panneau.util.LoginRequest;
import com.projet.gestion_panneau.util.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(request.getUsername());

        String token =
                jwtService.generateToken(userDetails);

        List<String> authorities =
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        LoginResponse response =
                LoginResponse.builder()
                        .token(token)
                        .username(userDetails.getUsername())
                        .authorities(authorities)
                        .build();

        return ResponseEntity.ok(response);
    }
}