package com.religuard.serviceImpl;

import com.religuard.common.enums.IncidentStatus;
import com.religuard.common.enums.IncidentType;
import com.religuard.common.enums.MonitoringStatus;
import com.religuard.common.enums.Status;
import com.religuard.config.RestTemplateConfig;
import com.religuard.domain.IncidentEntity;
import com.religuard.domain.UrlEntity;
import com.religuard.repository.IncidentRepository;
import com.religuard.repository.UrlRepository;
import jdk.jfr.Enabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class HealthCheckService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final int MAX_RETRIES = 3;
    private static final int FAILURE_THRESHOLD = 3;

    public void checkAllUrls() {

        List<UrlEntity> urls =
                urlRepository.findByStatusAndEnabled(Status.ACTIVE, true);

        for (UrlEntity url : urls) {

            MonitoringStatus previousStatus = url.getMonitoringStatus();

            HealthCheckResult result = checkUrlWithRetry(url);

            if (result.isSuccess()) {
                url.setFailureCount(0);
                url.setMonitoringStatus(MonitoringStatus.UP);
                urlRepository.save(url);

                if (previousStatus == MonitoringStatus.DOWN) {
                    resolveOpenIncident(url);
                }

            } else {
                int failures = url.getFailureCount() + 1;
                url.setFailureCount(failures);

                if (failures >= FAILURE_THRESHOLD) {
                    url.setMonitoringStatus(MonitoringStatus.DOWN);
                    urlRepository.save(url);

                    // Create incident based on failure type
                    if (result.getFailureType() == FailureType.TIMEOUT) {
                        createTimeoutIncident(url);
                    } else if (result.getFailureType() == FailureType.AUTH_ERROR) {
                        createAuthErrorIncident(url);
                    } else {
                        createIncidentIfNotExists(url);
                    }
                } else {
                    urlRepository.save(url);
                }
            }
        }
    }

    private HealthCheckResult checkUrlWithRetry(UrlEntity url) {

        HttpMethod method = HttpMethod.valueOf(url.getMethod());

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                HttpHeaders headers = new HttpHeaders();
                if (url.isAuthRequired()) {
                    headers.setBearerAuth(url.getAuthToken());
                }
                HttpEntity<Void> entity = new HttpEntity<>(headers);

                restTemplate.exchange(
                        url.getUrl(),
                        method,
                        entity,
                        String.class
                );
                return new HealthCheckResult(true, null);

            } catch (org.springframework.web.client.HttpClientErrorException.Unauthorized ex) {
                // Auth error detected - 401 Unauthorized
                urlRepository.save(url);
                return new HealthCheckResult(false, FailureType.AUTH_ERROR);

            } catch (org.springframework.web.client.ResourceAccessException ex) {
                // Timeout error detected
                if (ex.getCause() instanceof java.net.SocketTimeoutException ||
                    ex.getCause() instanceof java.io.InterruptedIOException) {
                    urlRepository.save(url);
                    return new HealthCheckResult(false, FailureType.TIMEOUT);
                }
                if (attempt == MAX_RETRIES) {
                    return new HealthCheckResult(false, FailureType.GENERAL);
                }
            } catch (Exception ex) {
                if (attempt == MAX_RETRIES) {
                    return new HealthCheckResult(false, FailureType.GENERAL);
                }
            }
        }
        return new HealthCheckResult(false, FailureType.GENERAL);
    }
    private void createIncidentIfNotExists(UrlEntity url) {

        List<IncidentEntity> openIncidents =
                incidentRepository.findByUrlIdAndStatus(
                        url.getId(),
                        IncidentStatus.OPEN
                );

        if (!openIncidents.isEmpty()) return;

        IncidentEntity incident = new IncidentEntity();
        incident.setUrlId(url.getId());
        incident.setUrl(url.getUrl());
        incident.setMonitoringStatus(MonitoringStatus.DOWN);
        incident.setType(IncidentType.URL_DOWN);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setStartedAt(LocalDateTime.now());

        incidentRepository.save(incident);
    }

    private void createAuthErrorIncident(UrlEntity url) {

        List<IncidentEntity> openAuthIncidents =
                incidentRepository.findByUrlIdAndStatusAndType(
                        url.getId(),
                        IncidentStatus.OPEN,
                        IncidentType.AUTH_ERROR
                );

        if (!openAuthIncidents.isEmpty()) return;

        IncidentEntity incident = new IncidentEntity();
        incident.setUrlId(url.getId());
        incident.setUrl(url.getUrl());
        incident.setMonitoringStatus(MonitoringStatus.DOWN);
        incident.setType(IncidentType.AUTH_ERROR);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setStartedAt(LocalDateTime.now());

        incidentRepository.save(incident);
    }

    private void createTimeoutIncident(UrlEntity url) {

        List<IncidentEntity> openTimeoutIncidents =
                incidentRepository.findByUrlIdAndStatusAndType(
                        url.getId(),
                        IncidentStatus.OPEN,
                        IncidentType.URL_TIMEOUT
                );

        if (!openTimeoutIncidents.isEmpty()) return;

        IncidentEntity incident = new IncidentEntity();
        incident.setUrlId(url.getId());
        incident.setUrl(url.getUrl());
        incident.setMonitoringStatus(MonitoringStatus.DOWN);
        incident.setType(IncidentType.URL_TIMEOUT);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setStartedAt(LocalDateTime.now());

        incidentRepository.save(incident);
    }

    private void resolveOpenIncident(UrlEntity url) {

        List<IncidentEntity> openIncidents =
                incidentRepository.findByUrlIdAndStatus(
                        url.getId(),
                        IncidentStatus.OPEN
                );

        for (IncidentEntity incident : openIncidents) {
            incident.setStatus(IncidentStatus.RESOLVED);
            incident.setResolvedAt(LocalDateTime.now());
            incident.setDurationInSeconds(
                    java.time.Duration.between(
                            incident.getStartedAt(),
                            incident.getResolvedAt()
                    ).getSeconds()
            );
            incidentRepository.save(incident);
        }

        // Reset failure count to 0 when incident is resolved
        url.setFailureCount(0);
        urlRepository.save(url);
    }

    // Helper class to track health check result and failure type
    private static class HealthCheckResult {
        private final boolean success;
        private final FailureType failureType;

        public HealthCheckResult(boolean success, FailureType failureType) {
            this.success = success;
            this.failureType = failureType;
        }

        public boolean isSuccess() {
            return success;
        }

        public FailureType getFailureType() {
            return failureType;
        }
    }

    /**
     * Check health of a specific URL by ID
     * @param urlId the ID of the URL to check
     * @throws IllegalArgumentException if URL not found
     */
    public void checkUrlById(String urlId) {
        UrlEntity url = urlRepository.findById(urlId)
                .orElseThrow(() -> new IllegalArgumentException("URL not found with ID: " + urlId));

        if (!url.isEnabled() || url.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("URL is not active or enabled");
        }

        MonitoringStatus previousStatus = url.getMonitoringStatus();
        HealthCheckResult result = checkUrlWithRetry(url);

        if (result.isSuccess()) {
            url.setFailureCount(0);
            url.setMonitoringStatus(MonitoringStatus.UP);
            urlRepository.save(url);

            if (previousStatus == MonitoringStatus.DOWN) {
                resolveOpenIncident(url);
            }
        } else {
            int failures = url.getFailureCount() + 1;
            url.setFailureCount(failures);

            if (failures >= FAILURE_THRESHOLD) {
                url.setMonitoringStatus(MonitoringStatus.DOWN);
                urlRepository.save(url);

                // Create incident based on failure type
                if (result.getFailureType() == FailureType.TIMEOUT) {
                    createTimeoutIncident(url);
                } else if (result.getFailureType() == FailureType.AUTH_ERROR) {
                    createAuthErrorIncident(url);
                } else {
                    createIncidentIfNotExists(url);
                }
            } else {
                urlRepository.save(url);
            }
        }
    }

    /**
     * Force check a specific URL, resetting failure count to trigger fresh checks
     * @param urlId the ID of the URL to force check
     * @throws IllegalArgumentException if URL not found
     */
    public void forceCheckUrl(String urlId) {
        UrlEntity url = urlRepository.findById(urlId)
                .orElseThrow(() -> new IllegalArgumentException("URL not found with ID: " + urlId));

        if (!url.isEnabled() || url.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("URL is not active or enabled");
        }

        // Reset failure count for fresh check
        url.setFailureCount(0);
        urlRepository.save(url);

        // Perform the health check
        checkUrlById(urlId);
    }

    /**
     * Simulate a timeout incident for a given URL (useful for tests).
     * This will set the failure count to the threshold, mark monitoring status as DOWN,
     * save the URL and create a URL_TIMEOUT incident if none exists open.
     */
    public void simulateTimeoutIncident(String urlId) {
        UrlEntity url = urlRepository.findById(urlId)
                .orElseThrow(() -> new IllegalArgumentException("URL not found with ID: " + urlId));

        // Ensure URL is active and enabled for simulation
        if (!url.isEnabled() || url.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("URL is not active or enabled");
        }

        // Force failure count to threshold and mark DOWN
        url.setFailureCount(FAILURE_THRESHOLD);
        url.setMonitoringStatus(MonitoringStatus.DOWN);
        urlRepository.save(url);

        // Create a timeout incident if none open exists
        createTimeoutIncident(url);
    }

    // Enum to categorize different types of failures
    private enum FailureType {
        TIMEOUT,      // Request timeout
        AUTH_ERROR,   // Authentication/Authorization error (401/403)
        GENERAL       // Other failures (connection error, etc.)
    }
}

