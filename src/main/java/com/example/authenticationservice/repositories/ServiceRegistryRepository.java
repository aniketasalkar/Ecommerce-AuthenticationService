package com.example.authenticationservice.repositories;

import com.example.authenticationservice.models.ServiceRegistry;
import com.example.authenticationservice.models.ServiceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRegistryRepository extends JpaRepository<ServiceRegistry, Long> {
    Optional<ServiceRegistry> findByServiceName(String serviceName);
    Optional<ServiceRegistry> findById(Long id);
    Optional<ServiceRegistry> findByIdAndServiceState(Long id, ServiceState state);
}
