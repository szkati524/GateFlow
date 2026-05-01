package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.config.PasswordConfig;
import com.gateflow.GateFlow.dto.PasswordChangeRequest;
import com.gateflow.GateFlow.model.User;
import com.gateflow.GateFlow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void changePassword(PasswordChangeRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
        if (!passwordEncoder.matches(request.oldPassword(),user.getPassword())) {
            throw new RuntimeException("Stare hasło jest niepoprawne!");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();

    }
    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id,User userDetails){
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDetails.getUsername());
        user.setFullName(userDetails.getFullName());
        user.setRole(userDetails.getRole());
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()){
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
       return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
