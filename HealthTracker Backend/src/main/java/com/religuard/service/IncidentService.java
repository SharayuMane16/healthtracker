package com.religuard.service;

import com.religuard.common.enums.IncidentStatus;
import com.religuard.domain.IncidentEntity;

import java.util.List;
import java.util.Optional;

public interface IncidentService {
    List<IncidentEntity> getIncidentsByService(String serviceId, IncidentStatus status);
    Optional<IncidentEntity> resolveIncident(String incidentId);

}
