package com.example.authenticationservice.dtos;

import com.example.authenticationservice.models.RequestStatus;
import lombok.Data;

@Data
public class LoginResponseDto {
    RequestStatus requestStatus;
}
