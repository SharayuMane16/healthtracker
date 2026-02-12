package com.religuard.repository;

import com.religuard.common.enums.Status;
import com.religuard.domain.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UrlRepository extends MongoRepository<UrlEntity,String> {
    List<UrlEntity> findByServiceId(String ServiceId);
    List<UrlEntity> findByEnabledTrueAndStatus(Status status);
    List<UrlEntity>findByStatusAndEnabled(Status status, Boolean enabled);

    // Check if URL already exists for a service
    boolean existsByServiceIdAndUrl(String serviceId, String url);
}
