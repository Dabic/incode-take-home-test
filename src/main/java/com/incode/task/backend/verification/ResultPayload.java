package com.incode.task.backend.verification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.incode.task.backend.client.CompanyDto;

import java.util.List;

public record ResultPayload(
        Object result,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<CompanyDto> otherResults
) {

}
