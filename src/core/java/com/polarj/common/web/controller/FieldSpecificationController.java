package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.FieldSpecification;
import com.polarj.model.service.FieldSpecificationService;

@RestController
@RequestMapping("/fieldspecs")
public class FieldSpecificationController extends ModelController<FieldSpecification, Integer, FieldSpecificationService>
{

    public FieldSpecificationController()
    {
        super(FieldSpecification.class);
    }
}