package com.incode.task.thirdparty.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record PremiumCompanyDto(
        String companyIdentificationNumber,
        String companyName,
        String registrationDate,
        String companyFullAddress,
        boolean isActive
) implements CompanyDto {}
