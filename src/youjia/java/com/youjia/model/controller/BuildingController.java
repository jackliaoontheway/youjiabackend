package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.Building;
import com.youjia.model.service.BuildingService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/buildings")
public class BuildingController
        extends ModelController<Building, Integer, BuildingService>
{
    public BuildingController()
    {
        super(Building.class);
    }
}
