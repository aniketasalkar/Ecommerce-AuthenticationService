package com.example.authenticationservice.utils;

import com.example.authenticationservice.dtos.ServiceRegistryRequestDto;
import com.example.authenticationservice.dtos.ServiceRegistryResponseDto;
import com.example.authenticationservice.models.ServiceRegistry;

public interface IDtoMapper {
    ServiceRegistry toServiceRegistry(ServiceRegistryRequestDto serviceRegistryRequestDto);
    ServiceRegistryResponseDto toServiceRegistryResponseDto(ServiceRegistry serviceRegistry);
}
