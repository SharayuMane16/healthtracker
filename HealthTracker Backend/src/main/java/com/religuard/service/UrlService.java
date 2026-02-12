package com.religuard.service;

import com.religuard.domain.UrlEntity;
import com.religuard.dto.CreateUrlRequest;
import com.religuard.dto.UpdateUrlRequest;

import java.util.List;

public interface UrlService {
    UrlEntity addUrl(String serviceId, CreateUrlRequest url);
    List<UrlEntity> getUrls(String serviceId);
    UrlEntity enableUrl(String id);
    UrlEntity disableUrl(String id);
    UrlEntity updateUrl(String id, UpdateUrlRequest request);
}
