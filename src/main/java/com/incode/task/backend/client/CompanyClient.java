package com.incode.task.backend.client;

import com.incode.task.backend.backend.ThirdPartyResultSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class CompanyClient {

    private static final String FREE_COMPANY_API = "/free-third-party";
    private static final String PREMIUM_COMPANY_API = "/premium-third-party";

    private static final ParameterizedTypeReference<List<CompanyDto>> companiesTypeReference = new ParameterizedTypeReference<>() {
    };
    private static final String QUERY_COMPANIES_QUERY_PARAM = "query";

    private final RestClient restClient;

    public CompanyClient(RestClient thirdPartyRestClient) {

        this.restClient = thirdPartyRestClient;
    }

    /**
     * I try to avoid putting business logic in the clients. But, for simplicity’s sake, here it infers the
     * outcome from the payload.
     */
    public CompanyQueryResult queryCompanies(ThirdPartyResultSource source, String query) {

        /*
         * In production-grade code, I would divide this client into two completely different clients, each with its
         * own configuration such as base url, connection pool, read and connection timeouts, etc.
         * Also, I certainly wouldn't use source argument like I did here. Instead, I would create a factory which would
         * return a correct client implementation based on the provided source.
         * But again, for simplicity’s sake, I kept it like this.
         */
        var endpoint = source == ThirdPartyResultSource.FREE ? FREE_COMPANY_API : PREMIUM_COMPANY_API;

        try {
            var response = restClient
                    .get()
                    .uri(builder -> builder
                            .path(endpoint)
                            .queryParam(QUERY_COMPANIES_QUERY_PARAM, query)
                            .build())
                    .retrieve()
                    .body(companiesTypeReference);

            var companies = Optional
                    .ofNullable(response)
                    .orElse(List.of())
                    .stream()
                    .filter(CompanyDto::isActive)
                    .toList();

            if (companies.isEmpty()) {
                return CompanyQueryResult.noResults();
            }

            return CompanyQueryResult.ok(companies);
        } catch (Exception e) {
            /*
             * RestClient throws on both 4xx and 5xx responses unless configured otherwise.
             * In production, I would handle these status codes differently (e.g., refresh-and-retry on 401, fail fast
             * on 400, etc.) instead of a generic catch.
             */
            return CompanyQueryResult.exceptionalResult();
        }
    }
}
