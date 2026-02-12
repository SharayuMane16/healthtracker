package com.religuard.serviceImpl;

import com.religuard.common.enums.IncidentStatus;
import com.religuard.domain.IncidentEntity;
import com.religuard.domain.UrlEntity;
import com.religuard.repository.IncidentRepository;
import com.religuard.repository.UrlRepository;
import com.religuard.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentServiceImpl implements IncidentService {
    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public List<IncidentEntity> getIncidentsByService(String serviceId, IncidentStatus status) {
       List<UrlEntity>urls=urlRepository.findByServiceId(serviceId);
       if(urls.isEmpty()){
           return List.of();
       }
       List<String>urlIds=urls.stream().map(UrlEntity::getId).toList();
        if (status == null) {
            return incidentRepository.findByUrlIdIn(urlIds);
        }
        return incidentRepository.findByUrlIdInAndStatus(urlIds,status);
    }

    @Override
    public Optional<IncidentEntity> resolveIncident(String incidentId) {
        Optional<IncidentEntity> incidentEntityOpt = incidentRepository.findById(incidentId);

        if (!incidentEntityOpt.isPresent()) {
            throw new RuntimeException("Incident not found with id: " + incidentId);
        }

        IncidentEntity incidentEntity = incidentEntityOpt.get();

        if (incidentEntity.getStatus() == IncidentStatus.RESOLVED) {
            return incidentEntityOpt;
        }

        incidentEntity.setStatus(IncidentStatus.RESOLVED);
        incidentEntity.setResolvedAt(LocalDateTime.now());

        long durationSeconds = Duration.between(
                incidentEntity.getStartedAt(),
                incidentEntity.getResolvedAt()
        ).getSeconds();

        incidentEntity.setDurationInSeconds(durationSeconds);

// ✅ Save to DB
        IncidentEntity savedIncident = incidentRepository.save(incidentEntity);

        return Optional.of(savedIncident);

    }
}
