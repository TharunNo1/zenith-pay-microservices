package com.zenithpay.identity_service.controller;

import com.zenithpay.identity_service.dto.AuthRequest;
import com.zenithpay.identity_service.model.UserCredential;
import com.zenithpay.identity_service.repository.IUserCredentialRepository;
import com.zenithpay.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody UserCredential userCredential) {
        return authService.saveUserCredential(userCredential);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        System.out.println("getToken: " + authRequest.toString());
        UserCredential user = repository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found in debug"));

        boolean match = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());

        System.out.println("--- DEBUG AUTH ---");
        System.out.println("Raw Password from Request: [" + authRequest.getPassword() + "]");
        System.out.println("Hash from DB: [" + user.getPassword() + "]");
        System.out.println("Do they match?: " + match);
        System.out.println("------------------");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                System.out.println(authRequest);
                System.out.println("Authentication Success: " + authentication);

                return authService.generateToken(authRequest.getUsername());
            }
            else {
                System.out.println("Authentication Failed");
                throw new RuntimeException("Invalid username or password");
            }

        } catch (Exception e) {
            System.out.println("Authentication Failed: " + e.getMessage());
            return "Failed";

        }

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        authService.validateToken(token);
        return "token validated";
    }



}
