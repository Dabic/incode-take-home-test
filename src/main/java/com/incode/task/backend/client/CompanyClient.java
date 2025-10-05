package com.incode.task.backend.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class CompanyClient {

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
    public CompanyQueryResult queryCompanies(String endpoint, String query) {

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
