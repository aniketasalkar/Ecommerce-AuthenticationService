package com.example.authenticationservice.repositories;

import com.example.authenticationservice.models.Session;
import com.example.authenticationservice.models.SessionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session save(Session session);
    Optional<Session> findById(Long sessionId);
    Optional<Session> findByUserAuthEmailAndSessionState(String userAuthEmail, SessionState sessionState);
    Optional<Session> findSessionByAccessTokenAndUserAuthEmail(String accessToken, String email);
    Optional<Session> findSessionByRefreshTokenAndAccessTokenAndSessionState(String refreshToken, String accessToken, SessionState sessionState);
}
