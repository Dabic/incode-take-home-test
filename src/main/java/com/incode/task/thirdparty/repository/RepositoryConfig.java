package com.incode.task.thirdparty.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class RepositoryConfig {

    @Bean
    public CompanyRepository freeCompanyRepository(
            @Value("${incode.third-party.free-service.repositories.file.path}") String filePath,
            ResourceLoader resourceLoader,
            ObjectMapper objectMapper) {

        return new FileCompanyRepository(resourceLoader.getResource(filePath), objectMapper);
    }

    @Bean
    public CompanyRepository premiumCompanyRepository(
            @Value("${incode.third-party.premium-service.repositories.file.path}") String filePath,
            ResourceLoader resourceLoader,
            ObjectMapper objectMapper) {

        return new FileCompanyRepository(resourceLoader.getResource(filePath), objectMapper);
    }
}
