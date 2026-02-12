package com.religuard.dto;

import com.religuard.common.enums.Status;

public class UpdateServiceRequest {

    private String name; // optional
    private Boolean enabled; // optional
    private Status status; // optional

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
