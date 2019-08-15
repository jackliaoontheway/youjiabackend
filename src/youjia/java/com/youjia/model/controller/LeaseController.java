package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.Lease;
import com.youjia.model.service.LeaseService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/leases")
public class LeaseController
        extends ModelController<Lease, Integer, LeaseService>
{
    public LeaseController()
    {
        super(Lease.class);
    }
}
