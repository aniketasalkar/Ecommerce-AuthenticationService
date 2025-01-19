package com.example.authenticationservice.services;

import com.example.authenticationservice.dtos.ValidateServiceTokenRequestDto;
import com.example.authenticationservice.exceptions.DoesNotExistsException;
import com.example.authenticationservice.exceptions.InvalidTokenException;
import com.example.authenticationservice.exceptions.ServiceAlreadyExistsException;
import com.example.authenticationservice.models.ServiceRegistry;
import com.example.authenticationservice.models.ServiceState;
import com.example.authenticationservice.repositories.ServiceRegistryRepository;
import com.example.authenticationservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.Date;

@Service
public class ServiceRegistryService implements IServiceRegistryService {

    @Autowired
    private ServiceRegistryRepository serviceRegistryRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ServiceRegistry registerService(ServiceRegistry serviceRegistry) {
        if (serviceRegistryRepository.findByIdAndServiceState(serviceRegistry.getId(), ServiceState.ACTIVE).isPresent()) {
            throw new ServiceAlreadyExistsException("Service already exists");
        }
        serviceRegistry.setServiceState(ServiceState.ACTIVE);

        String serviceToken = jwtUtils.generateServiceToken(serviceRegistry);
        serviceRegistry.setAccessToken(serviceToken);
        Date now = new Date();
        serviceRegistry.setCreatedAt(now);
        serviceRegistry.setUpdatedAt(now);

        return serviceRegistryRepository.save(serviceRegistry);
    }

    @Override
    public ServiceRegistry fetchServiceRegistry(String serviceName) {

        return serviceRegistryRepository.findByServiceName(serviceName).
                orElseThrow(() -> new DoesNotExistsException("Service not found"));
    }

    @Override
    public Boolean validateServiceRegistryToken(ValidateServiceTokenRequestDto validateServiceTokenRequestDto) {
        ServiceRegistry serviceRegistry = serviceRegistryRepository.findByServiceName(validateServiceTokenRequestDto.getServiceName()).
                orElseThrow(() -> new DoesNotExistsException("Service not found"));

        if (!serviceRegistry.getAccessToken().equals(validateServiceTokenRequestDto.getServiceToken())) {
            throw new InvalidTokenException("Invalid Service Token");
        }

        if (!jwtUtils.validateServiceToken(validateServiceTokenRequestDto.getServiceToken(), serviceRegistry)) {
            throw new InvalidTokenException("Invalid Service Token");
        }

        return true;
    }
}
