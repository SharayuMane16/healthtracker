package com.religuard.controller;

import com.religuard.domain.ServiceEntity;
import com.religuard.common.response.ApiResponse;
import com.religuard.dto.UpdateServiceRequest;
import com.religuard.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/services")
public class ServiceController {
    @Autowired
   ServiceService serviceService;

    @PostMapping("/createservice")
    public ResponseEntity<ApiResponse<ServiceEntity>> createService(
            @RequestBody ServiceEntity service) {

        ServiceEntity createdService = serviceService.createService(service);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Service created successfully",
                        createdService
                ));
    }


    @GetMapping("/allservices")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getAllServices() {

        List<ServiceEntity> services = serviceService.getAllServices();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Services fetched successfully",
                        services
                )
        );
    }

    @PatchMapping("/{serviceId}/enable")
    public ResponseEntity<ApiResponse<ServiceEntity>> enableService(
            @PathVariable String serviceId) {

        ServiceEntity updatedService = serviceService.enableServiced(serviceId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Service enabled successfully",
                        updatedService
                )
        );
    }

    @PatchMapping("/{serviceId}/disable")
    public ResponseEntity<ApiResponse<ServiceEntity>> disableService(
            @PathVariable String serviceId) {

        ServiceEntity updatedService = serviceService.disableService(serviceId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Service disabled successfully",
                        updatedService
                )
        );
    }


    @PatchMapping("/{serviceId}/delete")
    public ResponseEntity<ApiResponse<ServiceEntity>> deleteService(
            @PathVariable String serviceId) {

        ServiceEntity deletedService = serviceService.deleteService(serviceId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Service deleted successfully",
                        deletedService
                )
        );
    }
    @PatchMapping("updateService/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceEntity>> updateService(
            @PathVariable String serviceId,
            @RequestBody UpdateServiceRequest request) {

        ServiceEntity updatedService = serviceService.updateService(serviceId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Service updated successfully", updatedService)
        );
    }

}
