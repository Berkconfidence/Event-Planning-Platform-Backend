package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
    boolean existsByEmailAndUserIDNot(String email, Long userId);
    boolean existsByUsernameAndUserIDNot(String username, Long userId);
    boolean existsByPhoneNumberAndUserIDNot(String phoneNumber, Long userId);
    Optional<User> findByUsername(String username);

}
