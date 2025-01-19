package com.example.authenticationservice.services;

import com.example.authenticationservice.dtos.ValidateServiceTokenRequestDto;
import com.example.authenticationservice.models.ServiceRegistry;

public interface IServiceRegistryService {
    ServiceRegistry registerService(ServiceRegistry serviceRegistry);
    ServiceRegistry fetchServiceRegistry(String serviceName);
    Boolean validateServiceRegistryToken(ValidateServiceTokenRequestDto validateServiceTokenRequestDto);
}
