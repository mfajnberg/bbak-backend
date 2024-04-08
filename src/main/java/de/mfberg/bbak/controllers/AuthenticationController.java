package de.mfberg.bbak.controllers;

import de.mfberg.bbak.services.authentication.AuthenticationService;
import de.mfberg.bbak.dto.RegisterRequest;
import de.mfberg.bbak.dto.AuthenticationRequest;
import de.mfberg.bbak.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // todo: factor this check out into the authentication service
        if (service.isUserRegistered(request))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with this email already exists.");

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // todo: try and replace this if/else stuff with the global exception handling mechanism
        AuthenticationResponse response = service.refreshToken(request);
        if (response != null)
            return ResponseEntity.ok(response);
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Received an invalid refresh token.");
        }
    }
}
