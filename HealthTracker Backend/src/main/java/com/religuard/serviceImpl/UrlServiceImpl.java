package com.religuard.serviceImpl;

import com.religuard.common.enums.MonitoringStatus;
import com.religuard.common.enums.Status;
import com.religuard.domain.UrlEntity;
import com.religuard.dto.CreateUrlRequest;
import com.religuard.dto.UpdateUrlRequest;
import com.religuard.repository.UrlRepository;
import com.religuard.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    private UrlRepository urlRepository;

    @Override
    public UrlEntity addUrl(String serviceId, CreateUrlRequest urlReq) {
        UrlEntity url = new UrlEntity();
        url.setMethod(urlReq.getMethod());
        url.setUrl(urlReq.getUrl());
        url.setAuthRequired(urlReq.isAuthRequired());
        if(urlReq.isAuthRequired()){
            url.setAuthToken(urlReq.getAuthToken());
            url.setAuthType(urlReq.getAuthType());
        }
        url.setServiceId(serviceId);
        url.setStatus(Status.ACTIVE);
        url.setEnabled(true);
        url.setCreatedAt(LocalDateTime.now());
        url.setMonitoringStatus(MonitoringStatus.UNKNOWN);
        return urlRepository.save(url);
    }

    @Override
    public List<UrlEntity> getUrls(String serviceId) {
        return urlRepository.findByServiceId(serviceId);
    }

    @Override
    public UrlEntity enableUrl(String id) {
        UrlEntity url = urlRepository.findById(id).orElseThrow();
        url.setEnabled(true);
        return urlRepository.save(url);
    }

    @Override
    public UrlEntity disableUrl(String id) {
        UrlEntity url = urlRepository.findById(id).orElseThrow();
        url.setEnabled(false);
        return urlRepository.save(url);
    }

    @Override
    public UrlEntity updateUrl(String id, UpdateUrlRequest request) {
        UrlEntity existing = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL not found"));
        if (request.getUrl() != null && !request.getUrl().isBlank()) {
            existing.setUrl(request.getUrl());
        }
        if (request.getMethod() != null && !request.getMethod().isBlank()) {
            existing.setMethod(request.getMethod());
        }
        if (request.getEnabled() != null) {
            existing.setEnabled(request.getEnabled());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        if(request.getAuthToken()!=null){
            existing.setAuthToken(request.getAuthToken());
        }
        return urlRepository.save(existing);
    }
}

