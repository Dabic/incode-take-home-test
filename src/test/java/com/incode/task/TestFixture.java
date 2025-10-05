package com.incode.task;

import com.incode.task.backend.client.CompanyDto;
import com.incode.task.thirdparty.dto.FreeCompanyDto;
import com.incode.task.thirdparty.dto.PremiumCompanyDto;

public class TestFixture {

    public static final String QUERY = "S";

    private TestFixture() {

    }

    public static PremiumCompanyDto standardPremiumCompanyDto() {

        return new PremiumCompanyDto("Q436SGWY", "Gomez, Howard and Sanders", "2023-08-19", "204 Reyes Pines, Rachelville, WI 86404", true);
    }

    public static FreeCompanyDto standardFreeCompanyDto() {

        return new FreeCompanyDto("Q436SGWY", "Gomez, Howard and Sanders", "2023-08-19", "204 Reyes Pines, Rachelville, WI 86404", true);
    }

    public static CompanyDto standardCompanyDto() {

        return new CompanyDto("Q436SGWY", "Gomez, Howard and Sanders", "2023-08-19", "204 Reyes Pines, Rachelville, WI 86404", true);
    }

    public static FreeCompanyDto standardFreeCompanyDto2() {

        return new FreeCompanyDto("Q6KSPCQT", "Reyes, Nash and Gross", "2021-07-22", "4788 Allen Ways, Jeffreyfort, WI 11208", true);
    }

    public static CompanyDto standardCompanyDto2() {

        return new CompanyDto("Q6KSPCQT", "Reyes, Nash and Gross", "2021-07-22", "4788 Allen Ways, Jeffreyfort, WI 11208", true);
    }

    public static FreeCompanyDto standardFreeInActiveCompanyDto() {

        return new FreeCompanyDto("4A4U8MHS", "Dominguez, Harding and Chung", "2024-03-08", "56724 Hart Wall, Mendezstad, AZ 75095", false);
    }
}
