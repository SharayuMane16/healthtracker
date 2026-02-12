package com.religuard.repository;

import com.religuard.domain.ServiceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<ServiceEntity,String> {
    // Check if service name already exists
    boolean existsByName(String name);
}
