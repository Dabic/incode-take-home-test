package com.incode.task.thirdparty.service;

import com.incode.task.thirdparty.data.Company;
import com.incode.task.thirdparty.dto.CompanyDto;
import com.incode.task.thirdparty.mapper.CompanyDtoMapper;
import com.incode.task.thirdparty.repository.CompanyRepository;
import com.incode.task.thirdparty.simulator.ServiceUnavailableSimulator;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CompanyService {

    /**
     * In a real production-grade setup, I wouldn’t preload all companies into memory.
     * Depending on requirements, I would either use a cache designed for this type of search or query the database
     * directly for each request.
     */
    private final List<Company> companies;
    private final CompanyDtoMapper mapper;
    private final ServiceUnavailableSimulator serviceUnavailableSimulator;
    private final int outageProbability;

    protected CompanyService(
            CompanyDtoMapper mapper,
            int outageProbability,
            ServiceUnavailableSimulator serviceUnavailableSimulator,
            CompanyRepository companyRepository) {

        this.companies = List.copyOf(companyRepository.findAll());
        this.mapper = mapper;
        this.outageProbability = outageProbability;
        this.serviceUnavailableSimulator = serviceUnavailableSimulator;
    }

    @Transactional(readOnly = true)
    public List<CompanyDto> findAllByIdentificationNumberContaining(@NonNull String identificationNumberFragment) {

        serviceUnavailableSimulator.maybeSimulateOutage(outageProbability);

        String normalizedQuery = identificationNumberFragment.toUpperCase();

        return companies.stream()
                .filter(company -> company.identificationNumber().contains(normalizedQuery))
                .map(mapper::map)
                .toList();
    }
}
