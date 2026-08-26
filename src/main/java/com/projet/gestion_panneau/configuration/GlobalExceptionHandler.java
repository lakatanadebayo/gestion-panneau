package com.projet.gestion_panneau.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 - Erreur de validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElse(null);

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("status", 400);
        error.put("message", fieldError != null ? fieldError.getDefaultMessage() : "Erreur de validation");
        error.put("field", fieldError != null ? fieldError.getField() : null);

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 405 - Méthode HTTP non autorisée
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("status", 405);
        error.put("message", "Méthode HTTP non autorisée");
        error.put("method", ex.getMethod());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    /**
     * 500 - Erreur interne du serveur
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("status", 500);
        error.put("message", "Une erreur interne est survenue");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
