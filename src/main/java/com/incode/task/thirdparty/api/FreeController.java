package com.incode.task.thirdparty.api;

import com.incode.task.thirdparty.service.CompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.incode.task.thirdparty.api.ApiConstants.FREE_COMPANY_API;

@RestController
@RequestMapping(FREE_COMPANY_API)
public class FreeController extends CompanyController {

    public FreeController(CompanyService freeCompanyService) {

        super(freeCompanyService);
    }
}
