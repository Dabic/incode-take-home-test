package com.incode.task.backend.backend;

import com.incode.task.backend.client.CompanyClient;
import com.incode.task.backend.client.QueryStatus;
import com.incode.task.backend.verification.VerificationService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BackendService {

    public static final String NO_RESULTS_FOUND_MSG = "No results found";
    public static final String UPSTREAMS_DOWN_MSG = "Upstreams down";

    private final CompanyClient companyClient;
    private final VerificationService verificationService;

    public BackendService(CompanyClient companyClient, VerificationService verificationService) {

        this.companyClient = companyClient;
        this.verificationService = verificationService;
    }

    public BackendDto queryCompanies(String query, UUID verificationId) {

        Pair<BackendDto, ThirdPartyResultSource> queryResultAndSource = queryCompanySources(query, verificationId);

        var result = queryResultAndSource.getLeft();
        var source = queryResultAndSource.getRight();

        /*
         * In a production-grade code, query auditing like this one is a side effect and should ideally be decoupled
         * from the core flow.
         */
        verificationService.storeVerification(verificationId, query, source, result);

        return result;
    }

    /**
     * In a production-grade code, I would probably use Spring Retry for this. But since our requirements are not
     * that complex i.e., there is no retry backoff or a need for circuit breaking, this implementation is enough.
     */
    private Pair<BackendDto, ThirdPartyResultSource> queryCompanySources(String query, UUID verificationId) {

        var freeCompanyResult = companyClient.queryCompanies(ThirdPartyResultSource.FREE, query);
        if (QueryStatus.OK.equals(freeCompanyResult.status())) {
            return Pair.of(
                    BackendDto.of(query, verificationId, freeCompanyResult.companies()),
                    ThirdPartyResultSource.FREE
            );
        }

        var premiumCompanyResult = companyClient.queryCompanies(ThirdPartyResultSource.PREMIUM, query);
        if (QueryStatus.OK.equals(premiumCompanyResult.status())) {
            return Pair.of(
                    BackendDto.of(query, verificationId, premiumCompanyResult.companies()),
                    ThirdPartyResultSource.PREMIUM
            );
        }

        if (QueryStatus.NO_RESULTS.equals(freeCompanyResult.status()) ||
                QueryStatus.NO_RESULTS.equals(premiumCompanyResult.status())) {
            return Pair.of(BackendDto.of(query, verificationId, NO_RESULTS_FOUND_MSG), null);
        } else {
            return Pair.of(BackendDto.of(query, verificationId, UPSTREAMS_DOWN_MSG), null);
        }
    }
}
