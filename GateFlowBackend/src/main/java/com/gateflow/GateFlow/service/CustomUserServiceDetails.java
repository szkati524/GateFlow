package com.gateflow.GateFlow.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserServiceDetails implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    public CustomUserServiceDetails(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
if ("security".equals(username)) {
    return User.builder()
            .username("security")
            .password(passwordEncoder.encode("security1"))
            .roles("SECURITY")
            .build();
}
if ("admin".equals(username)){
    return User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin1"))
            .roles("ADMIN")
            .build();
}
throw new UsernameNotFoundException("User not found: " + username);
    }
}
