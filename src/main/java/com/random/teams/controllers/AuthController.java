package com.random.teams.controllers;

import com.random.teams.models.LoginRequest;
import com.random.teams.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:19006/", "192.168.0.9:8081"})
@RestController
@RequestMapping("api/login")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> validationCredentials(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.validationCredentials(loginRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password is incorrect");
        }
    }
}
