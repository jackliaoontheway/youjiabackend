package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.I18nResource;
import com.polarj.model.service.I18nResourceService;

@RestController
@RequestMapping("/i18nresources")
public class I18nResourceController extends ModelController<I18nResource, Integer, I18nResourceService>
{

    public I18nResourceController()
    {
        super(I18nResource.class);
    }
}