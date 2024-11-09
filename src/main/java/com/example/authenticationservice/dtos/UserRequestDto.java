package com.example.authenticationservice.dtos;

import com.example.authenticationservice.models.RequestStatus;
import lombok.Data;

@Data
public class UserRequestDto {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
