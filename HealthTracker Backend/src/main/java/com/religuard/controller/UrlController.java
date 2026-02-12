package com.religuard.controller;

import com.religuard.domain.UrlEntity;
import com.religuard.dto.CreateUrlRequest;
import com.religuard.common.response.ApiResponse;
import com.religuard.dto.UpdateUrlRequest;
import com.religuard.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/services/{serviceId}/urls")
public class UrlController {

    @Autowired
    private UrlService urlService;

    // ADD URL TO SERVICE
    @PostMapping
    public ResponseEntity<ApiResponse<UrlEntity>> addUrl(
            @PathVariable String serviceId,
            @Valid @RequestBody CreateUrlRequest request) {

        UrlEntity createdUrl = urlService.addUrl(serviceId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "URL added successfully",
                        createdUrl
                ));
    }

    // GET ALL URLS FOR A SERVICE
    @GetMapping
    public ResponseEntity<ApiResponse<List<UrlEntity>>> getUrls(
            @PathVariable String serviceId) {

        List<UrlEntity> urls = urlService.getUrls(serviceId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "URLs fetched successfully",
                        urls
                )
        );
    }

    // ENABLE URL
    @PatchMapping("/{urlId}/enable")
    public ResponseEntity<ApiResponse<UrlEntity>> enableUrl(
            @PathVariable String urlId) {

        UrlEntity updatedUrl = urlService.enableUrl(urlId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "URL enabled successfully",
                        updatedUrl
                )
        );
    }

    // DISABLE URL
    @PatchMapping("/{urlId}/disable")
    public ResponseEntity<ApiResponse<UrlEntity>> disableUrl(
            @PathVariable String urlId) {

        UrlEntity updatedUrl = urlService.disableUrl(urlId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "URL disabled successfully",
                        updatedUrl
                )
        );
    }

    // PARTIAL UPDATE (PATCH)
    @PatchMapping("/{urlId}")
    public ResponseEntity<ApiResponse<UrlEntity>> updateUrl(
            @PathVariable String urlId,
            @RequestBody UpdateUrlRequest request) {

        UrlEntity updatedUrl = urlService.updateUrl( urlId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "URL updated successfully", updatedUrl)
        );
    }
}



