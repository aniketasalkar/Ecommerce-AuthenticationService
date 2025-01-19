package com.example.authenticationservice.utils;

import com.example.authenticationservice.dtos.ServiceRegistryRequestDto;
import com.example.authenticationservice.dtos.ServiceRegistryResponseDto;
import com.example.authenticationservice.models.ServiceRegistry;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper implements IDtoMapper{

    @Override
    public ServiceRegistry toServiceRegistry(ServiceRegistryRequestDto serviceRegistryRequestDto) {
        ServiceRegistry serviceRegistry = new ServiceRegistry();
        serviceRegistry.setServiceName(serviceRegistryRequestDto.getServiceName());
        serviceRegistry.setServiceDescription(serviceRegistryRequestDto.getServiceDescription());

        return serviceRegistry;
    }

    @Override
    public ServiceRegistryResponseDto toServiceRegistryResponseDto(ServiceRegistry serviceRegistry) {
        ServiceRegistryResponseDto serviceRegistryResponseDto = new ServiceRegistryResponseDto();
        serviceRegistryResponseDto.setServiceId(serviceRegistry.getId());
        serviceRegistryResponseDto.setServiceName(serviceRegistry.getServiceName());
        serviceRegistryResponseDto.setServiceDescription(serviceRegistry.getServiceDescription());
        serviceRegistryResponseDto.setServiceToken(serviceRegistry.getAccessToken());

        return serviceRegistryResponseDto;
    }
}
