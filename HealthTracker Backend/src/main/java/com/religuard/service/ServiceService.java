package com.religuard.service;

import com.religuard.domain.ServiceEntity;
import com.religuard.dto.UpdateServiceRequest;

import java.util.List;

public interface ServiceService {
    ServiceEntity createService(ServiceEntity service);
    List<ServiceEntity>getAllServices();
    ServiceEntity enableServiced(String serviceId);
    ServiceEntity disableService(String serviceId);
    ServiceEntity deleteService(String serviceId);
    ServiceEntity updateService(String serviceId, UpdateServiceRequest request);
}
