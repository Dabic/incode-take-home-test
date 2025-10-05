package com.incode.task.thirdparty.mapper;

import com.incode.task.thirdparty.data.Company;
import com.incode.task.thirdparty.dto.CompanyDto;

public interface CompanyDtoMapper {

    CompanyDto map(Company company);
}
