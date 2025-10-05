package com.incode.task.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incode.task.backend.backend.BackendDto;
import com.incode.task.backend.backend.ThirdPartyResultSource;
import com.incode.task.backend.client.CompanyDto;
import com.incode.task.backend.verification.VerificationDto;
import com.incode.task.thirdparty.BaseIntegrationTest;
import com.incode.task.thirdparty.api.FreeController;
import com.incode.task.thirdparty.api.PremiumController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.incode.task.TestFixture.standardCompanyDto;
import static com.incode.task.TestFixture.standardCompanyDto2;
import static com.incode.task.TestFixture.standardFreeCompanyDto;
import static com.incode.task.TestFixture.standardFreeCompanyDto2;
import static com.incode.task.TestFixture.standardFreeInActiveCompanyDto;
import static com.incode.task.backend.backend.ApiConstants.BACKEND_SERVICE_API;
import static com.incode.task.backend.backend.BackendService.NO_RESULTS_FOUND_MSG;
import static com.incode.task.backend.backend.BackendService.UPSTREAMS_DOWN_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@Sql(scripts = "/fixture/verification_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTest extends BaseIntegrationTest {

    private static final ResponseEntity<List<com.incode.task.thirdparty.dto.CompanyDto>> SERVICE_UNAVAILABLE_RESPONSE = ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .build();
    private static final String COMPANY_QUERY = "query123";
    private static final UUID VERIFICATION_ID = UUID.randomUUID();
    private static final ParameterizedTypeReference<BackendDto> DTO_TYPE = new ParameterizedTypeReference<>() {
    };

    private static final String URI_STRING = UriComponentsBuilder
            .fromUriString(BACKEND_SERVICE_API)
            .queryParam("query", COMPANY_QUERY)
            .queryParam("verificationId", VERIFICATION_ID)
            .toUriString();

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestRestTemplate restTemplate;

    @MockitoBean
    protected FreeController freeController;

    @MockitoBean
    protected PremiumController premiumController;

    @Test
    void freeNonEmptyResponse_shouldNotCallPremium() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(standardFreeCompanyDto())));

        restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        verify(freeController, times(1)).getCompanies(COMPANY_QUERY);
        verifyNoInteractions(premiumController);
    }

    @Test
    void freeEmptyResponse_shouldCallPremium() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of()));

        restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        verify(freeController, times(1)).getCompanies(COMPANY_QUERY);
        verify(premiumController, times(1)).getCompanies(COMPANY_QUERY);
    }

    @Test
    void freeExceptionalResponse_shouldCallPremium() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(SERVICE_UNAVAILABLE_RESPONSE);

        restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        verify(freeController, times(1)).getCompanies(COMPANY_QUERY);
        verify(premiumController, times(1)).getCompanies(COMPANY_QUERY);
    }

    @Test
    void bothFreeAndPremiumExceptionalResponse_shouldReturnResultWithMessage() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(SERVICE_UNAVAILABLE_RESPONSE);
        when(premiumController.getCompanies(COMPANY_QUERY))
                .thenReturn(SERVICE_UNAVAILABLE_RESPONSE);

        var response = restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().result()).isEqualTo(UPSTREAMS_DOWN_MSG);
        assertThat(response.getBody().otherResults()).isNull();
    }

    @Test
    void bothFreeAndPremiumEmptyResponse_shouldReturnResultWithMessage() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of()));
        when(premiumController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of()));

        var response = restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().result()).isEqualTo(NO_RESULTS_FOUND_MSG);
        assertThat(response.getBody().otherResults()).isNull();
    }

    @Test
    void singleCompanyThirdPartyResponse_shouldReturnResultAndNoOtherResults() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(standardFreeCompanyDto())));

        var response = restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().otherResults()).isNull();

        var actualResult = objectMapper.convertValue(response.getBody().result(), CompanyDto.class);
        assertThat(actualResult).isEqualTo(standardCompanyDto());
    }

    @Test
    void multipleCompaniesThirdPartyResponse_shouldReturnResultAndOtherResults() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(
                        standardFreeCompanyDto(),
                        standardFreeCompanyDto2(),
                        standardFreeInActiveCompanyDto())));

        var response = restTemplate.exchange(
                URI_STRING,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().otherResults()).isEqualTo(List.of(standardCompanyDto2()));

        var actualResult = objectMapper.convertValue(response.getBody().result(), CompanyDto.class);
        assertThat(actualResult).isEqualTo(standardCompanyDto());
    }

    @Test
    void verificationDoesNotExist_shouldReturnNotFound() {

        var verification = restTemplate.exchange(
                "/verifications/" + UUID.randomUUID(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );

        assertThat(verification.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void freeNonEmptyResponse_shouldStoreVerificationWithFreeAsSource() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(standardFreeCompanyDto())));

        var uuid = UUID.randomUUID();

        String uriString = UriComponentsBuilder
                .fromUriString(BACKEND_SERVICE_API)
                .queryParam("query", COMPANY_QUERY)
                .queryParam("verificationId", uuid)
                .toUriString();

        restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        var verification = restTemplate.exchange(
                "/verifications/" + uuid,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );

        assertThat(verification.getBody().source()).isEqualTo(ThirdPartyResultSource.FREE);
    }

    @Test
    void freeEmptyResponseAndPremiumNotEmptyResponse_shouldStoreVerificationWithPremiumAsSource() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of()));
        when(premiumController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(standardFreeCompanyDto())));

        var uuid = UUID.randomUUID();

        String uriString = UriComponentsBuilder
                .fromUriString(BACKEND_SERVICE_API)
                .queryParam("query", COMPANY_QUERY)
                .queryParam("verificationId", uuid)
                .toUriString();

        restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        var verification = restTemplate.exchange(
                "/verifications/" + uuid,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );

        assertThat(verification.getBody().source()).isEqualTo(ThirdPartyResultSource.PREMIUM);
    }

    @Test
    void singleCompanyThirdPartyResponse_shouldStoreVerificationWithResult() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(standardFreeCompanyDto())));

        var uuid = UUID.randomUUID();

        String uriString = UriComponentsBuilder
                .fromUriString(BACKEND_SERVICE_API)
                .queryParam("query", COMPANY_QUERY)
                .queryParam("verificationId", uuid)
                .toUriString();

        restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        var verification = restTemplate.exchange(
                "/verifications/" + uuid,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );

        var body = verification.getBody();

        assertThat(body.verificationId()).isEqualTo(uuid);
        assertThat(body.queryText()).isEqualTo(COMPANY_QUERY);
        assertThat(body.result().otherResults()).isNull();

        var actualResult = objectMapper.convertValue(body.result().result(), CompanyDto.class);
        assertThat(actualResult).isEqualTo(standardCompanyDto());
    }

    @Test
    void multipleCompaniesThirdPartyResponse_shouldStoreVerificationWithResultAndSourceAndOtherResults() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of(
                        standardFreeCompanyDto(),
                        standardFreeCompanyDto2())));

        var uuid = UUID.randomUUID();

        String uriString = UriComponentsBuilder
                .fromUriString(BACKEND_SERVICE_API)
                .queryParam("query", COMPANY_QUERY)
                .queryParam("verificationId", uuid)
                .toUriString();

        restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        var verification = restTemplate.exchange(
                "/verifications/" + uuid,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );
        var body = verification.getBody();

        assertThat(body.verificationId()).isEqualTo(uuid);
        assertThat(body.queryText()).isEqualTo(COMPANY_QUERY);

        var actualResult = objectMapper.convertValue(body.result().result(), CompanyDto.class);
        assertThat(actualResult).isEqualTo(standardCompanyDto());

        var actualOtherResults = objectMapper.convertValue(body.result().otherResults(), new TypeReference<List<CompanyDto>>() {});
        assertThat(actualOtherResults).isEqualTo(List.of(standardCompanyDto2()));
    }

    @Test
    void bothFreeAndPremiumExceptionalResponse_shouldStoreVerificationWithResult() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(SERVICE_UNAVAILABLE_RESPONSE);
        when(premiumController.getCompanies(COMPANY_QUERY))
                .thenReturn(SERVICE_UNAVAILABLE_RESPONSE);

        var uuid = UUID.randomUUID();

        String uriString = UriComponentsBuilder
                .fromUriString(BACKEND_SERVICE_API)
                .queryParam("query", COMPANY_QUERY)
                .queryParam("verificationId", uuid)
                .toUriString();

        restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        var verification = restTemplate.exchange(
                "/verifications/" + uuid,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );

        var body = verification.getBody();

        assertThat(body.verificationId()).isEqualTo(uuid);
        assertThat(body.queryText()).isEqualTo(COMPANY_QUERY);
        assertThat(body.source()).isNull();
        assertThat(body.result().result()).isEqualTo(UPSTREAMS_DOWN_MSG);
        assertThat(body.result().otherResults()).isNull();
    }

    @Test
    void bothFreeAndPremiumEmptyResponse_shouldStoreVerificationWithResult() {

        when(freeController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of()));
        when(premiumController.getCompanies(COMPANY_QUERY))
                .thenReturn(ResponseEntity.ok(List.of()));

        var uuid = UUID.randomUUID();

        String uriString = UriComponentsBuilder
                .fromUriString(BACKEND_SERVICE_API)
                .queryParam("query", COMPANY_QUERY)
                .queryParam("verificationId", uuid)
                .toUriString();

        restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                DTO_TYPE
        );

        var verification = restTemplate.exchange(
                "/verifications/" + uuid,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                VerificationDto.class
        );

        var body = verification.getBody();

        assertThat(body.verificationId()).isEqualTo(uuid);
        assertThat(body.queryText()).isEqualTo(COMPANY_QUERY);
        assertThat(body.source()).isNull();
        assertThat(body.result().result()).isEqualTo(NO_RESULTS_FOUND_MSG);
        assertThat(body.result().otherResults()).isNull();
    }
}
