package com.religuard.domain;

import com.religuard.common.enums.MonitoringStatus;
import com.religuard.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "urls")
public class UrlEntity {
    @Id
    private String id;
    private String serviceId;
    @NotBlank(message = "Url name cannot be null or blank")
    private String url;

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    private int failureCount=0;


    public MonitoringStatus getMonitoringStatus() {
        return monitoringStatus;
    }

    public void setMonitoringStatus(MonitoringStatus monitoringStatus) {
        this.monitoringStatus = monitoringStatus;
    }

    private MonitoringStatus monitoringStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @NotBlank(message = "Method name cannot be null or blank")
    private String method;
    private Status status;
    private boolean enabled;
    private LocalDateTime createdAt;

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private boolean authRequired;
    private String authType;      // BEARER, BASIC (future)
    private String authToken;     // encrypted later
}

