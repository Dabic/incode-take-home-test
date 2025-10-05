package com.incode.task.thirdparty.repository;

import com.incode.task.thirdparty.data.Company;

import java.util.List;

public interface CompanyRepository {

    List<Company> findAll();
}
