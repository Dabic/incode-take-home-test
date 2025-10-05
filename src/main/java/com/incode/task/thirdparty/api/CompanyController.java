package com.incode.task.thirdparty.api;

import com.incode.task.thirdparty.dto.CompanyDto;
import com.incode.task.thirdparty.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public abstract class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {

        this.companyService = companyService;
    }

    /**
     * In production-grade setup, I would provide Swagger documentation.
     */
    @GetMapping
    public ResponseEntity<List<CompanyDto>> getCompanies(@RequestParam(value = "query") String query) {

        var companies = companyService.findAllByIdentificationNumberContaining(query);

        return ResponseEntity.ok(companies);
    }
}
