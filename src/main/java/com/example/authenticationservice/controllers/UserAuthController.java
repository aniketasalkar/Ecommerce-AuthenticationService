package com.example.authenticationservice.controllers;

import com.example.authenticationservice.clients.UserManagementServiceClient;
import com.example.authenticationservice.dtos.*;
import com.example.authenticationservice.models.RequestStatus;
import com.example.authenticationservice.models.TokenState;
import com.example.authenticationservice.services.IUserAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {
    @Autowired
    IUserAuthService userAuthService;

    @Autowired
    UserManagementServiceClient userManagementServiceClient;

    @PostMapping("/signUp")
    public ResponseEntity<SignupResponseDto> signUpUser(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        try {
            SignupResponseDto signupResponse = userAuthService.signup(signupRequestDto);

            signupResponse.setRequestStatus(RequestStatus.SUCCESS);

            return new ResponseEntity<>(signupResponse, HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String token = userAuthService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        String[] tokens = token.split(":");
        String accessToken = tokens[0];
        String refreshToken = tokens[1];

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, accessToken);
        headers.add(HttpHeaders.SET_COOKIE2, refreshToken);
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
//        headers.add(HttpHeaders.);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setRequestStatus(RequestStatus.SUCCESS);

        return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{email}/logout")
    public ResponseEntity<LogOutResponseDto> logoutUser (@RequestHeader("Set-Cookie") String authHeader,
                                                         @PathVariable String email,
                                                         HttpServletResponse httpServletResponse) {
        try {
            Boolean isLoggedOut = userAuthService.logout(email, authHeader);

            if (isLoggedOut) {
                httpServletResponse.setHeader("Set-Cookie", "");
                httpServletResponse.setHeader("Set-Cookie2", "");
//                clearCookies(httpServletResponse);
            }
            LogOutResponseDto logOutResponseDto = new LogOutResponseDto();
            logOutResponseDto.setRequestStatus(RequestStatus.SUCCESS);

            return new ResponseEntity<>(logOutResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/{email}/validateAndRefreshToken")
    public ResponseEntity<ValidateAndRefreshTokenResponseDto> validateAndRefreshToken(@PathVariable String email, @RequestBody @Valid ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto) {
        try {
            Pair<TokenState, String> tokenValidity = userAuthService.validateAndRefreshToken(email, validateAndRefreshTokenRequestDto);

            ValidateAndRefreshTokenResponseDto validateAndRefreshTokenResponseDto = new ValidateAndRefreshTokenResponseDto();
            validateAndRefreshTokenResponseDto.setTokenState(tokenValidity.a);
            validateAndRefreshTokenResponseDto.setAccessToken(tokenValidity.b);
            validateAndRefreshTokenResponseDto.setRefreshToken(validateAndRefreshTokenRequestDto.getRefreshToken());
            validateAndRefreshTokenResponseDto.setRequestStatus(RequestStatus.SUCCESS);

            return new ResponseEntity<>(validateAndRefreshTokenResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/{email}/validateToken")
    public ResponseEntity<Boolean> validateToken(@PathVariable String email, @RequestBody @Valid ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto) {
        try {
            Pair<TokenState, String> tokenValidity = userAuthService.validateAndRefreshToken(email, validateAndRefreshTokenRequestDto);
            Boolean isTokenValid = Boolean.FALSE;
            if (tokenValidity.a.equals(TokenState.ACTIVE)) {
                isTokenValid = Boolean.TRUE;
            }

            return new ResponseEntity<>(isTokenValid, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

//    private void clearCookies(HttpServletResponse response) {
//        Cookie cookie = new Cookie("access_token", null);
//        cookie.setPath("/"); // Set the path to match the cookie path
//        cookie.setHttpOnly(true); // Make it HTTP only
//        cookie.setMaxAge(0); // Set cookie age to 0 to delete it
//        response.addCookie(cookie);
//
//        // Repeat for refresh token or any other cookies you want to clear
//        Cookie refreshCookie = new Cookie("refresh_token", null);
//        refreshCookie.setPath("/");
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setMaxAge(0);
//        response.addCookie(refreshCookie);
//    }

}
