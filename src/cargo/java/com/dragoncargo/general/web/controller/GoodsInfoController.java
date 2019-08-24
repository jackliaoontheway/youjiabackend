package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.GoodsInfo;
import com.dragoncargo.general.model.service.GoodsInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/goodsinfos")
public class GoodsInfoController
        extends ModelController<GoodsInfo, Integer, GoodsInfoService>
{
    public GoodsInfoController()
    {
        super(GoodsInfo.class);
    }
}
