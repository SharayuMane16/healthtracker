package com.religuard.controller;

import com.religuard.common.enums.IncidentStatus;
import com.religuard.domain.IncidentEntity;
import com.religuard.common.response.ApiResponse;
import com.religuard.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/services/{serviceId}/incidents")
public class IncidentController {
    @Autowired IncidentService incidentService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<IncidentEntity>>>getIncidentsByService(@PathVariable String serviceId, @RequestParam(required = false)IncidentStatus status){
        List<IncidentEntity>incidentEntityList=incidentService.getIncidentsByService(serviceId,status);
        return ResponseEntity.ok(new ApiResponse<>(true,"Incidents fetched successfully",incidentEntityList));
    }
    @PatchMapping("/{incidentId}/resolve")
    public ResponseEntity<ApiResponse<Optional<IncidentEntity>>> resolveIncident(
            @PathVariable String incidentId) {

        Optional<IncidentEntity> resolved = incidentService.resolveIncident(incidentId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Incident resolved manually",
                        resolved
                )
        );
    }




}
