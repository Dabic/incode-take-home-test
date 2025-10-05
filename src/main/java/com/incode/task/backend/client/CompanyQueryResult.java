package com.incode.task.backend.client;

import java.util.List;

public record CompanyQueryResult(QueryStatus status, List<CompanyDto> companies) {

    public static CompanyQueryResult ok(List<CompanyDto> companies) {

        return new CompanyQueryResult(QueryStatus.OK, companies);
    }

    public static CompanyQueryResult noResults() {

        return new CompanyQueryResult(QueryStatus.NO_RESULTS, null);
    }

    public static CompanyQueryResult exceptionalResult() {

        return new CompanyQueryResult(QueryStatus.EXCEPTIONAL_RESPONSE, null);
    }
}