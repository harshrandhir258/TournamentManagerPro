package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.entity.Role;
import com.harsh.tournamentmanagerpro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1️⃣ Admin can create a new user (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // 2️⃣ Admin can get list of all users (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 3️⃣ Admin can delete a user by ID (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 4️⃣ Get currently logged-in user info (any authenticated user)
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    // 5️⃣ Get users by role (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

}

