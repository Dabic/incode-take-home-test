package com.incode.task.backend.verification;

import com.incode.task.backend.backend.ThirdPartyResultSource;

import java.time.LocalDateTime;
import java.util.UUID;

public record VerificationDto(
        UUID verificationId,
        String queryText,
        LocalDateTime timestamp,
        ThirdPartyResultSource source,
        BackendQueryResult result
) {

    public VerificationDto(Verification verification) {
        this(
                verification.getVerificationId(),
                verification.getQueryText(),
                verification.getTimestamp(),
                verification.getSource(),
                verification.getResult()
        );
    }
}
