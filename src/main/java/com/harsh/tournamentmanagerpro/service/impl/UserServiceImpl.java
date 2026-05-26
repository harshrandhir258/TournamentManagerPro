package com.harsh.tournamentmanagerpro.service.impl;

import com.harsh.tournamentmanagerpro.entity.Role;
import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.repository.UserRepository;
import com.harsh.tournamentmanagerpro.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // ✅ PasswordEncoder import
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ✅ Added PasswordEncoder field

    // ✅ Constructor injection for UserRepository and PasswordEncoder
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // ✅ Injected
    }

    // ✅ Encode password before saving user
    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ Encode password here
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found: " + username));
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }
}
