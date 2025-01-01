package com.example.authenticationservice.services;

import com.example.authenticationservice.clients.UserManagementServiceClient;
import com.example.authenticationservice.dtos.*;
import com.example.authenticationservice.exceptions.*;
import com.example.authenticationservice.models.Session;
import com.example.authenticationservice.models.SessionState;
import com.example.authenticationservice.models.TokenState;
import com.example.authenticationservice.models.UserAuth;
import com.example.authenticationservice.repositories.SessionRepository;
import com.example.authenticationservice.repositories.UserAuthRepository;
import com.example.authenticationservice.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class UserAuthService implements IUserAuthService {

    @Autowired
    UserAuthRepository authRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserManagementServiceClient userManagementServiceClient;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Transactional
//    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 5000))
    @Override
    public SignupResponseDto signup(SignupRequestDto user) {

        if (authRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        UserResponseDto userResponseDto = new UserResponseDto();
        try {
            userResponseDto = userManagementServiceClient.createUser(from(user));
            Date now = new Date();

            UserAuth userAuth = new UserAuth();
            userAuth.setId(userResponseDto.getId());
            userAuth.setEmail(user.getEmail());
            userAuth.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userAuth.setCreatedAt(now);
            userAuth.setUpdatedAt(now);
            userAuth.setLastLogin(now);

            authRepository.save(userAuth);
            userManagementServiceClient.sendWelcomeEmail(user.getEmail());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("deleting user");
            userManagementServiceClient.deleteUser(user.getEmail());
            userAuthRepository.deleteByEmail(user.getEmail());
        }

        return to(userResponseDto);
    }

    @Transactional
    @Override
    public String login(String email, String password) {

        UserAuth userCreds = authRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!bCryptPasswordEncoder.matches(password, userCreds.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        UserResponseDto userDetails = userManagementServiceClient.getUser(userCreds.getEmail().toString());
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        Date now = new Date();

        userCreds.setLastLogin(now);
        userAuthRepository.save(userCreds);

        Session session = new Session();
        session.setUserAuth(userCreds);
        session.setRefreshToken(refreshToken);
        session.setAccessToken(accessToken);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setSessionState(SessionState.ACTIVE);

        if (sessionRepository.findByUserAuthEmailAndSessionState(userCreds.getEmail(), SessionState.ACTIVE).isPresent()) {
            Session prevSession = sessionRepository.findByUserAuthEmailAndSessionState(userCreds.getEmail(), SessionState.ACTIVE).get();
            prevSession.setSessionState(SessionState.INACTIVE);
            prevSession.setUpdatedAt(new Date());
            sessionRepository.save(prevSession);
        }

        sessionRepository.save(session);


        return accessToken + ":" + refreshToken;
    }

    @Override
    public Pair<TokenState, String> validateAndRefreshToken(String email, ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto) {
        Pair<TokenState, String> response = new Pair<>(TokenState.ACTIVE, validateAndRefreshTokenRequestDto.getAccessToken());

        UserResponseDto userDetails = userManagementServiceClient.getUser(email);

        log.info("Passed Access Token: {}", validateAndRefreshTokenRequestDto.getAccessToken().toString());
        log.info("Passed Refresh Token: {}", validateAndRefreshTokenRequestDto.getRefreshToken().toString());

        Session session = sessionRepository.findSessionByRefreshTokenAndAccessTokenAndSessionState(
                validateAndRefreshTokenRequestDto.getRefreshToken(),
                validateAndRefreshTokenRequestDto.getAccessToken(),
                SessionState.ACTIVE
                ).orElseThrow(() -> new InvalidTokenException("Invalid token"));

        log.info("Stored Access Token: {}", session.getAccessToken());
        log.info("Stored Refresh Token: {}", session.getRefreshToken());

        if (jwtUtils.validateToken("RefreshToken", session.getRefreshToken(), userDetails)) {
            if (!jwtUtils.validateToken("AccessToken", session.getAccessToken(), userDetails)) {
                String newAccessToken = jwtUtils.generateAccessToken(userDetails);
                session.setAccessToken(newAccessToken);
                session.setUpdatedAt(new Date());

                sessionRepository.save(session);
                response = new Pair<>(TokenState.REFRESHED, newAccessToken);
            }
        } else {
            session.setSessionState(SessionState.INACTIVE);
            session.setUpdatedAt(new Date());

            sessionRepository.save(session);
            response = new Pair<>(TokenState.EXPIRED, "");
        }

        return response;
    }

    @Override
    public Boolean logout(String email, String token) {
        UserAuth userCreds = userAuthRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        Session session = sessionRepository.findSessionByAccessTokenAndUserAuthEmail(token, userCreds.getEmail()).orElseThrow(() ->
                new InvalidTokenException("Invalid token"));

        if (session.getSessionState().equals(SessionState.INACTIVE)) {
            throw new NoActiveSessionFoundException("No active session found with found token");
        }

        session.setSessionState(SessionState.INACTIVE);
        session.setUpdatedAt(new Date());
        sessionRepository.save(session);

        return true;
    }

    private UserRequestDto from(SignupRequestDto user) {
        UserRequestDto userDto = new UserRequestDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());

        return userDto;
    }

    private SignupResponseDto to(UserResponseDto userResponseDto) {
        SignupResponseDto signupResponseDto = new SignupResponseDto();
        signupResponseDto.setId(userResponseDto.getId());
        signupResponseDto.setEmail(userResponseDto.getEmail());
        signupResponseDto.setFirstName(userResponseDto.getFirstName());
        signupResponseDto.setLastName(userResponseDto.getLastName());
        signupResponseDto.setPhoneNumber(userResponseDto.getPhoneNumber());

        return signupResponseDto;
    }
}
