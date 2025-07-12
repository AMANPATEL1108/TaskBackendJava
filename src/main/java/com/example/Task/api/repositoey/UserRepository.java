package com.example.Task.api.repositoey;

import com.example.Task.api.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findUserById(Integer id);
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);


}

