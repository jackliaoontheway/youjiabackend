package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.Scheduler;
import com.polarj.model.service.SchedulerService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/schedulers")
public class SchedulerController
        extends ModelController<Scheduler, Integer, SchedulerService>
{
    public SchedulerController()
    {
        super(Scheduler.class);
    }
}
