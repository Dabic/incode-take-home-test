package com.incode.task.thirdparty;

import com.incode.task.thirdparty.dto.FreeCompanyDto;
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
import static com.incode.task.TestFixture.standardFreeCompanyDto;
import static com.incode.task.thirdparty.api.ApiConstants.FREE_COMPANY_API;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;

public class ThirdPartyFreeIntegrationTest extends BaseIntegrationTest {

    private static final ParameterizedTypeReference<List<FreeCompanyDto>> DTO_TYPE = new ParameterizedTypeReference<>() {
    };

    @Test
    void noMatchingCompanies_shouldReturnEmptyCompanies() {

        var uri = UriComponentsBuilder.fromUriString(FREE_COMPANY_API)
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

        var uri = UriComponentsBuilder.fromUriString(FREE_COMPANY_API)
                .queryParam("query", QUERY)
                .toUriString();

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(4);
        assertThat(response.getBody())
                .extracting(FreeCompanyDto::cin)
                .allMatch(cin -> cin.contains(QUERY));

        // just to be sure all other fields are mapped correctly
        assertThat(response.getBody().getFirst())
                .isEqualTo(standardFreeCompanyDto());
    }

    @Test
    void shouldSimulateServiceUnavailable() {

        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE))
                .when(outageSimulator)
                .maybeSimulateOutage(anyInt());

        var uri = UriComponentsBuilder.fromUriString(FREE_COMPANY_API)
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
