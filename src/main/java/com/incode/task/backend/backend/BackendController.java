package com.incode.task.backend.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.incode.task.backend.backend.ApiConstants.BACKEND_SERVICE_API;

@RequestMapping(BACKEND_SERVICE_API)
@RestController
public class BackendController {

    private final BackendService backendService;

    public BackendController(BackendService backendService) {

        this.backendService = backendService;
    }

    /**
     * In production-grade setup, I would provide Swagger documentation.
     */
    @GetMapping
    public ResponseEntity<BackendDto> queryCompanies(
            @RequestParam String query,
            @RequestParam UUID verificationId) {

        return ResponseEntity.ok(backendService.queryCompanies(query, verificationId));
    }
}
