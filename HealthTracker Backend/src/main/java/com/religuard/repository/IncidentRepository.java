package com.religuard.repository;

import com.religuard.common.enums.IncidentStatus;
import com.religuard.common.enums.IncidentType;
import com.religuard.domain.IncidentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IncidentRepository extends MongoRepository<IncidentEntity,String> {

    // Find open incidents for a specific URL
    List<IncidentEntity> findByUrlIdAndStatus(String urlId, IncidentStatus incidentStatus);

    // Find open incidents for a specific URL with specific type
    List<IncidentEntity> findByUrlIdAndStatusAndType(String urlId, IncidentStatus incidentStatus, IncidentType incidentType);

    List<IncidentEntity> findByUrlIdInAndStatus(List<String>urlIds,IncidentStatus status);
    List<IncidentEntity> findByUrlIdIn(List<String>urlIds);

}
