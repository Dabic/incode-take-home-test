package com.incode.task.backend.client;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CompanyDto(
        @JsonAlias({"cin", "companyIdentificationNumber"}) String identificationNumber,
        @JsonAlias({"companyName"}) String name,
        @JsonAlias("registration_date") String registrationDate,
        @JsonAlias({"companyFullAddress"}) String address,
        @JsonAlias("is_active") boolean isActive
) {

}
