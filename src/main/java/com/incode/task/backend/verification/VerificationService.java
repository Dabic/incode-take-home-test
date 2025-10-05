package com.incode.task.backend.verification;

import com.incode.task.backend.backend.BackendDto;
import com.incode.task.backend.backend.ThirdPartyResultSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;

    public VerificationService(VerificationRepository verificationRepository) {

        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public void storeVerification(UUID id, String queryText, ThirdPartyResultSource source, BackendDto resultDto) {

        ResultPayload resultPayload = new ResultPayload(resultDto.result(), resultDto.otherResults());

        /*
         * Using LocalDateTime.now() makes it impossible to test. If I was writing this for production,
         * I would inject Clock into the component and use LocalDateTime.now(clock) which would make this test-friendly.
         */
        verificationRepository.save(new Verification(id, queryText, LocalDateTime.now(), source, resultPayload));
    }

    @Transactional(readOnly = true)
    public Optional<VerificationDto> findById(UUID id) {

        return verificationRepository
                .findById(id)
                .map(VerificationDto::new);
    }

}
