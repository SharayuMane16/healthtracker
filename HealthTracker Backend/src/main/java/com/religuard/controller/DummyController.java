package com.religuard.controller;

import com.religuard.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/dummy")
public class DummyController {

    /**
     * Dummy endpoint to simulate a URL timeout scenario.
     * Responds after ~5 seconds.
     * GET /dummy/url_timeout
     */
    @GetMapping("/url_timeout")
    public ResponseEntity<ApiResponse<String>> urlTimeout() {
        try {
            // Sleep for 5 seconds to simulate timeout
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Interrupted during simulated delay", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Simulated timeout response", "OK"));
    }
}

