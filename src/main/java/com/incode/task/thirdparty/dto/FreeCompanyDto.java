package com.incode.task.thirdparty.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FreeCompanyDto(
        String cin,
        String name,
        String registrationDate,
        String address,
        boolean isActive
) implements CompanyDto {}
