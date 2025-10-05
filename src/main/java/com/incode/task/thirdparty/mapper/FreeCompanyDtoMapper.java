package com.incode.task.thirdparty.mapper;

import com.incode.task.thirdparty.data.Company;
import com.incode.task.thirdparty.dto.CompanyDto;
import com.incode.task.thirdparty.dto.FreeCompanyDto;
import org.springframework.stereotype.Component;

@Component
public class FreeCompanyDtoMapper implements CompanyDtoMapper {

    @Override
    public CompanyDto map(Company company) {

        return new FreeCompanyDto(
                company.identificationNumber(),
                company.name(),
                company.registrationDate(),
                company.address(),
                company.isActive());
    }
}
