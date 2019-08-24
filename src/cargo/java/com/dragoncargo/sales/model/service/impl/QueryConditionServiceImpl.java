package com.dragoncargo.sales.model.service.impl;

import com.dragoncargo.sales.model.QuoteChargeItem;
import com.dragoncargo.sales.model.QuoteFreightFee;
import com.dragoncargo.sales.model.QuoteScheme;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.sales.model.QueryCondition;
import com.dragoncargo.sales.model.service.QueryConditionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueryConditionServiceImpl extends EntityServiceImpl<QueryCondition, Integer>
        implements QueryConditionService
{

}
