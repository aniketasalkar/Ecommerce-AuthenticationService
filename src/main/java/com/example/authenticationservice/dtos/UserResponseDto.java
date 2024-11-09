package com.example.authenticationservice.dtos;

import com.example.authenticationservice.models.RequestStatus;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private RequestStatus requestStatus;
}
