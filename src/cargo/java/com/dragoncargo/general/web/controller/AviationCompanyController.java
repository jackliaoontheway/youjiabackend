package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.AviationCompany;
import com.dragoncargo.general.model.service.AviationCompanyService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/aviationcompanys")
public class AviationCompanyController
        extends ModelController<AviationCompany, Integer, AviationCompanyService>
{
    public AviationCompanyController()
    {
        super(AviationCompany.class);
    }
}
