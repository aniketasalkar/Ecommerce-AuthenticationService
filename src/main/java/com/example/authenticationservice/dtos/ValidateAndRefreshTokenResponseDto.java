package com.example.authenticationservice.dtos;

import com.example.authenticationservice.models.RequestStatus;
import com.example.authenticationservice.models.TokenState;
import lombok.Data;

@Data
public class ValidateAndRefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private RequestStatus requestStatus;
    private TokenState tokenState;
}
