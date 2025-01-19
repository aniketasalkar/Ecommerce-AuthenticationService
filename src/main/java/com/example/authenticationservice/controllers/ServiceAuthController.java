package com.example.authenticationservice.controllers;

import com.example.authenticationservice.dtos.ServiceRegistryRequestDto;
import com.example.authenticationservice.dtos.ServiceRegistryResponseDto;
import com.example.authenticationservice.dtos.ValidateServiceTokenRequestDto;
import com.example.authenticationservice.services.IServiceRegistryService;
import com.example.authenticationservice.utils.IDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/service")
public class ServiceAuthController {

    @Autowired
    private IServiceRegistryService serviceRegistryService;

    @Autowired
    private IDtoMapper dtoMapper;

    @PostMapping("/register")
    public ResponseEntity<ServiceRegistryResponseDto> registerService(@RequestBody ServiceRegistryRequestDto serviceRegistryRequestDto) {
        new ServiceRegistryResponseDto();
        ServiceRegistryResponseDto serviceRegistryResponseDto;

        try {
            serviceRegistryResponseDto = dtoMapper.toServiceRegistryResponseDto(
                    serviceRegistryService.registerService(dtoMapper.toServiceRegistry(serviceRegistryRequestDto)));

            return new ResponseEntity<>(serviceRegistryResponseDto, HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/fetch_token/{service_name}")
    public ResponseEntity<ServiceRegistryResponseDto> fetchServiceRegistryToken(@PathVariable String service_name) {
        ServiceRegistryResponseDto serviceRegistryResponseDto;
        try {
            serviceRegistryResponseDto = dtoMapper.toServiceRegistryResponseDto(
                    serviceRegistryService.fetchServiceRegistry(service_name));

            return new ResponseEntity<>(serviceRegistryResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/validate_token/")
    public ResponseEntity<Boolean> validateServiceRegistryToken(@RequestBody ValidateServiceTokenRequestDto validateServiceTokenRequestDto) {
        try {
            Boolean result = serviceRegistryService.validateServiceRegistryToken(validateServiceTokenRequestDto);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
