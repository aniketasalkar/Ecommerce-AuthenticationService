package com.example.authenticationservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ValidateServiceTokenRequestDto {
    @NotBlank(message = "serviceName cannot be blank")
    private String serviceName;

    @NotBlank(message = "serviceToken cannot be blank")
    private String serviceToken;
}
