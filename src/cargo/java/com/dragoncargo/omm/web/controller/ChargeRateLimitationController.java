package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.ChargeRateLimitation;
import com.dragoncargo.omm.model.service.ChargeRateLimitationService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/chargeratelimitations")
public class ChargeRateLimitationController
        extends ModelController<ChargeRateLimitation, Integer, ChargeRateLimitationService>
{
    public ChargeRateLimitationController()
    {
        super(ChargeRateLimitation.class);
    }
}
