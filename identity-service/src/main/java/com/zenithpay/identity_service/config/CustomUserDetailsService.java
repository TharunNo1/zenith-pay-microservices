package com.zenithpay.identity_service.config;

import com.zenithpay.identity_service.model.UserCredential;
import com.zenithpay.identity_service.repository.IUserCredentialRepository;
import com.zenithpay.identity_service.model.CustomUserDetails;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("Loaded User from DB: " + user.getUsername());
        System.out.println("Stored Hash: " + user.getPassword());

        return new CustomUserDetails(user);

    }



}