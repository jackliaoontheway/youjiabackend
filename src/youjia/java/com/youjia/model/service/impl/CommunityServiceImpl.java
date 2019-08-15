package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Community;
import com.youjia.model.service.CommunityService;

@Service
public class CommunityServiceImpl extends EntityServiceImpl<Community, Integer>
        implements CommunityService
{
}
