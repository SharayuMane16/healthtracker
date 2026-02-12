package com.religuard.dto;

import com.religuard.common.enums.Status;
import jakarta.validation.constraints.Pattern;

public class UpdateUrlRequest {

    private String url;  // optional
    @Pattern(regexp = "GET|POST|PUT|DELETE|PATCH", message = "Method must be valid HTTP method")
    private String method; // optional
    private Boolean enabled; // optional
    private Status status; // optional

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String authToken;

    // getters and setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status;


    }
}
