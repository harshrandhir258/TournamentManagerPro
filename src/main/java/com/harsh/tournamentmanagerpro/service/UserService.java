package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.Role;
import com.harsh.tournamentmanagerpro.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    List<User> getUsersByRole(Role role);

    // Auth-related methods
    User getCurrentUser();
    boolean isAdmin(User user);
}
