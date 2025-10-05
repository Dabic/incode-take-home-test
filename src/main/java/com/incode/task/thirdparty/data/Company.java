package com.incode.task.thirdparty.data;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Company(
        @JsonAlias({"cin", "companyIdentificationNumber"})
        String identificationNumber,

        @JsonAlias("companyName")
        String name,

        @JsonAlias("registration_date")
        String registrationDate,

        @JsonAlias("fullAddress")
        String address,

        @JsonAlias("is_active")
        boolean isActive
) {

}
