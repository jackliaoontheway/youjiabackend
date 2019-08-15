package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.Community;
import com.youjia.model.service.CommunityService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/communitys")
public class CommunityController
        extends ModelController<Community, Integer, CommunityService>
{
    public CommunityController()
    {
        super(Community.class);
    }
}
