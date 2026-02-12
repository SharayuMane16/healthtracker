package com.religuard.serviceImpl;

import com.religuard.common.enums.Status;
import com.religuard.domain.ServiceEntity;
import com.religuard.dto.UpdateServiceRequest;
import com.religuard.repository.ServiceRepository;
import com.religuard.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    ServiceRepository serviceRepository;


    @Override
    public ServiceEntity createService(ServiceEntity service) {
        service.setStatus(Status.ACTIVE);
        service.setEnabled(true);
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    @Override
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public ServiceEntity enableServiced(String serviceId) {
        ServiceEntity service=serviceRepository.findById(serviceId).orElseThrow();
        service.setEnabled(true);
        service.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    @Override
    public ServiceEntity disableService(String serviceId) {
        ServiceEntity service = serviceRepository.findById(serviceId).orElseThrow();
        service.setEnabled(false);
        service.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    @Override
    public ServiceEntity deleteService(String serviceId) {
        ServiceEntity service = serviceRepository.findById(serviceId).orElseThrow();
        service.setStatus(Status.DELETED);
        service.setEnabled(false);
        service.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }
    public ServiceEntity updateService(String serviceId, UpdateServiceRequest request) {
        ServiceEntity existing = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName());
        }
        if (request.getEnabled() != null) {
            existing.setEnabled(request.getEnabled());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        return serviceRepository.save(existing);
    }

}
