package com.incode.task.thirdparty;

import com.incode.task.thirdparty.dto.PremiumCompanyDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.incode.task.TestFixture.QUERY;
import static com.incode.task.TestFixture.standardPremiumCompanyDto;
import static com.incode.task.thirdparty.api.ApiConstants.PREMIUM_COMPANY_API;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;

public class ThirdPartyPremiumIntegrationTest extends BaseIntegrationTest {

    private static final ParameterizedTypeReference<List<PremiumCompanyDto>> DTO_TYPE = new ParameterizedTypeReference<>() {
    };

    @Test
    void noMatchingCompanies_shouldReturnEmptyCompanies() {

        var uri = UriComponentsBuilder.fromUriString(PREMIUM_COMPANY_API)
                .queryParam("query", UUID.randomUUID())
                .toUriString();

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void matchedCompanies_shouldReturnCompanies() {

        var uri = UriComponentsBuilder.fromUriString(PREMIUM_COMPANY_API)
                .queryParam("query", QUERY)
                .toUriString();

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(8);
        assertThat(response.getBody())
                .extracting(PremiumCompanyDto::companyIdentificationNumber)
                .allMatch(cin -> cin.contains(QUERY));

        // just to be sure all other fields are mapped correctly
        assertThat(response.getBody().get(1))
                .isEqualTo(standardPremiumCompanyDto());
    }

    @Test
    void shouldSimulateServiceUnavailable() {

        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE))
                .when(outageSimulator)
                .maybeSimulateOutage(anyInt());

        var uri = UriComponentsBuilder.fromUriString(PREMIUM_COMPANY_API)
                .queryParam("query", QUERY)
                .toUriString();

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
