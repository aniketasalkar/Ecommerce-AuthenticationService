package com.example.authenticationservice.repositories;

import com.example.authenticationservice.models.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    UserAuth save(UserAuth userAuth);
    Optional<UserAuth> findByEmail(String email);
}
