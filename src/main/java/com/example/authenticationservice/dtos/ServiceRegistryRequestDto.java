package com.example.authenticationservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ServiceRegistryRequestDto {
    @NotEmpty(message = "Service Name required")
    private String serviceName;

    @NotEmpty(message = "Service description required")
    private String serviceDescription;

//    @NotBlank(message = )
//    private String serviceType;
}
