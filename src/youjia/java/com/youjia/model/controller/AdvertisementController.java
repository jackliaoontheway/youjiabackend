package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.Advertisement;
import com.youjia.model.service.AdvertisementService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController
        extends ModelController<Advertisement, Integer, AdvertisementService>
{
    public AdvertisementController()
    {
        super(Advertisement.class);
    }
}
