package com.incode.task.thirdparty.api;

import com.incode.task.thirdparty.service.CompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.incode.task.thirdparty.api.ApiConstants.PREMIUM_COMPANY_API;

@RestController
@RequestMapping(PREMIUM_COMPANY_API)
public class PremiumController extends CompanyController {

    public PremiumController(CompanyService premiumCompanyService) {

        super(premiumCompanyService);
    }
}
