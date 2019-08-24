package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.ModelSpecification;
import com.polarj.model.service.ModelSpecificationService;

@RestController
@RequestMapping("/modelspecs")
public class ModelSpecificationController extends ModelController<ModelSpecification, Integer, ModelSpecificationService>
{

    public ModelSpecificationController()
    {
        super(ModelSpecification.class);
    }
}