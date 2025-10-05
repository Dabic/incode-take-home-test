package com.incode.task.backend.backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.incode.task.backend.client.CompanyDto;

import java.util.List;
import java.util.UUID;

public record BackendDto(
        String query,
        UUID verificationId,
        @JsonInclude(JsonInclude.Include.NON_NULL) Object result,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<CompanyDto> otherResults,
        @JsonIgnore ThirdPartyResultSource source
) {

    public static BackendDto of(String query, UUID verificationId, List<CompanyDto> companies) {

        var result = companies.getFirst();
        List<CompanyDto> otherResults = null;

        if (companies.size() > 1) {
            otherResults = companies.subList(1, companies.size());
        }

        return new BackendDto(query, verificationId, result, otherResults, null);
    }

    public static BackendDto of(String query, UUID verificationId, String message) {

        return new BackendDto(query, verificationId, message, null, null);
    }
}
