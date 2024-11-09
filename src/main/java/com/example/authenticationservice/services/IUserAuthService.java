package com.example.authenticationservice.services;

import com.example.authenticationservice.dtos.SignupRequestDto;
import com.example.authenticationservice.dtos.SignupResponseDto;
import com.example.authenticationservice.dtos.ValidateAndRefreshTokenRequestDto;
import com.example.authenticationservice.models.TokenState;
import org.antlr.v4.runtime.misc.Pair;

public interface IUserAuthService {
    SignupResponseDto signup(SignupRequestDto user);
    String login(String email, String password);
    Pair<TokenState, String> validateAndRefreshToken(String email, ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto);
    Boolean logout(String email, String token);
}
