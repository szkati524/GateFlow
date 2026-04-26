package com.gateflow.GateFlow.service;



import com.gateflow.GateFlow.model.User;
import com.gateflow.GateFlow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserServiceDetails implements UserDetailsService {


    private final UserRepository userRepository;


    public CustomUserServiceDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika " + username));
String roleName = user.getRole().name();
if (roleName.startsWith("ROLE_")) {
    roleName = roleName.replace("ROLE_","");
}
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }
}
