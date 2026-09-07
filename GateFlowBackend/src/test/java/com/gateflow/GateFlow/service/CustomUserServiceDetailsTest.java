package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.enums.Role;
import com.gateflow.GateFlow.model.User;
import com.gateflow.GateFlow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserServiceDetailsTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserServiceDetails customUserServiceDetails;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists_AndRoleHasPrefix() {

        User user = new User();
        user.setUsername("jan.kowalski");
        user.setPassword("secret_password");
        user.setRole(com.gateflow.GateFlow.enums.Role.ROLE_ADMIN);

        when(userRepository.findByUsername("jan.kowalski")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = customUserServiceDetails.loadUserByUsername("jan.kowalski");

        // Assert
        assertNotNull(result);
        assertEquals("jan.kowalski", result.getUsername());
        assertEquals("secret_password", result.getPassword());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        verify(userRepository, times(1)).findByUsername("jan.kowalski");
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists_AndRoleHasNoPrefix() {

        User user = new User();
        user.setUsername("anna.nowak");
        user.setPassword("secret_password");
        user.setRole(Role.ROLE_SECURITY);

        when(userRepository.findByUsername("anna.nowak")).thenReturn(Optional.of(user));


        UserDetails result = customUserServiceDetails.loadUserByUsername("anna.nowak");


        assertNotNull(result);
        assertEquals("anna.nowak", result.getUsername());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SECURITY")));
        verify(userRepository, times(1)).findByUsername("anna.nowak");
    }

    @Test
    void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {

        when(userRepository.findByUsername("nieznany")).thenReturn(Optional.empty());


        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserServiceDetails.loadUserByUsername("nieznany")
        );

        assertEquals("Nie znaleziono użytkownika nieznany", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nieznany");
    }
}

