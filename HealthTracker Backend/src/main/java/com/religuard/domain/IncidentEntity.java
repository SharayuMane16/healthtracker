package com.religuard.domain;

import com.religuard.common.enums.IncidentStatus;
import com.religuard.common.enums.IncidentType;
import com.religuard.common.enums.MonitoringStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "incidents")
public class IncidentEntity {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;
    private String urlId;
    private String url;
    private MonitoringStatus monitoringStatus;
    private IncidentType type;

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MonitoringStatus getMonitoringStatus() {
        return monitoringStatus;
    }

    public void setMonitoringStatus(MonitoringStatus monitoringStatus) {
        this.monitoringStatus = monitoringStatus;
    }

    public IncidentType getType() {
        return type;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    private IncidentStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime resolvedAt;
    private Long durationInSeconds;
}
