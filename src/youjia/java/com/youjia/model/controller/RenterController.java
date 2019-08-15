package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.Renter;
import com.youjia.model.service.RenterService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/renters")
public class RenterController
        extends ModelController<Renter, Integer, RenterService>
{
    public RenterController()
    {
        super(Renter.class);
    }
}
