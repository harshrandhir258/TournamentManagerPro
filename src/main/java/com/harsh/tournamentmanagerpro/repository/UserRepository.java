package com.harsh.tournamentmanagerpro.repository;

import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);  // ye naya add kiya role-based filtering ke liye
}
