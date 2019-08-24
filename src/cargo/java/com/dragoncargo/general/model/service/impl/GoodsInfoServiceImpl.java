package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.GoodsInfo;
import com.dragoncargo.general.model.service.GoodsInfoService;

@Service
public class GoodsInfoServiceImpl extends EntityServiceImpl<GoodsInfo, Integer>
        implements GoodsInfoService
{
}
