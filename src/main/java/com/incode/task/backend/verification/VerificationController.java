package com.incode.task.backend.verification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {

        this.verificationService = verificationService;
    }

    /**
     * In production-grade setup, I would provide Swagger documentation.
     */
    @GetMapping("/verifications/{verificationId}")
    public ResponseEntity<VerificationDto> getVerificationById(@PathVariable UUID verificationId) {

        return verificationService
                .findById(verificationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
