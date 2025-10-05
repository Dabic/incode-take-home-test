package com.incode.task.thirdparty.service;

import com.incode.task.thirdparty.mapper.CompanyDtoMapper;
import com.incode.task.thirdparty.repository.CompanyRepository;
import com.incode.task.thirdparty.simulator.ServiceUnavailableSimulator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CompanyService freeCompanyService(
            CompanyDtoMapper freeCompanyDtoMapper,
            CompanyRepository freeCompanyRepository,
            ServiceUnavailableSimulator simulator,
            @Value("${incode.third-party.free-service.outage-probability:0}") int outageProbability
    ) {

        return new CompanyService(freeCompanyDtoMapper, outageProbability, simulator, freeCompanyRepository);
    }

    @Bean
    public CompanyService premiumCompanyService(
            CompanyDtoMapper premiumCompanyDtoMapper,
            CompanyRepository premiumCompanyRepository,
            ServiceUnavailableSimulator simulator,
            @Value("${incode.third-party.premium-service.outage-probability:0}") int outageProbability
    ) {

        return new CompanyService(premiumCompanyDtoMapper, outageProbability, simulator, premiumCompanyRepository);
    }
}
