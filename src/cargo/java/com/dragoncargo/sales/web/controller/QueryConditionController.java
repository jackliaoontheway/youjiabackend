package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.*;

import com.dragoncargo.sales.model.QueryCondition;
import com.dragoncargo.sales.model.service.QueryConditionService;
import com.polarj.common.web.controller.ModelController;


@RestController
@RequestMapping("/queryconditions")
public class QueryConditionController
        extends ModelController<QueryCondition, Integer, QueryConditionService>
{
    public QueryConditionController()
    {
        super(QueryCondition.class);
    }

}
