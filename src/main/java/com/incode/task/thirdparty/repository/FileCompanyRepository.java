package com.incode.task.thirdparty.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incode.task.thirdparty.data.Company;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public class FileCompanyRepository implements CompanyRepository {

    private final Resource resource;
    private final ObjectMapper objectMapper;

    public FileCompanyRepository(Resource resource, ObjectMapper objectMapper) {

        this.resource = resource;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Company> findAll() {

        try (var in = resource.getInputStream()) {
            return objectMapper.readValue(in, new TypeReference<>() {});
        } catch (IOException e) {
            /*
             * In production-grade code, this should be handled more gracefully. However, if the service cannot
             * connect to or load data from its datasource, it often makes sense to fail fast at startup rather
             * than continue in a broken state.
             */
            throw new RuntimeException(e);
        }
    }
}
