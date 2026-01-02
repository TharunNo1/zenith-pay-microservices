package com.zenithpay.identity_service.service;

import com.zenithpay.identity_service.model.UserCredential;
import com.zenithpay.identity_service.repository.IUserCredentialRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private IUserCredentialRepository userCredentialRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public String saveUserCredential(UserCredential credential) {
            credential.setPassword(passwordEncoder.encode(credential.getPassword()));
            userCredentialRepository.save(credential);
            return "User added successfully";
    }

    public String generateToken(String username) {
        System.out.println("Generating token for user: " + username);
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) throws JwtException, IllegalArgumentException {
        jwtService.validateToken(token);
    }
}

