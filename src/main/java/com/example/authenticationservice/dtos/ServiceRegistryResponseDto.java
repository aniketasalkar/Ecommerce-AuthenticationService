package com.example.authenticationservice.dtos;

import lombok.Data;

@Data
public class ServiceRegistryResponseDto {
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceToken;
}
