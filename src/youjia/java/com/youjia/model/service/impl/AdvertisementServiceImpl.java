package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Advertisement;
import com.youjia.model.service.AdvertisementService;

@Service
public class AdvertisementServiceImpl extends EntityServiceImpl<Advertisement, Integer>
        implements AdvertisementService
{
}
