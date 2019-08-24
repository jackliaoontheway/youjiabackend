package com.dragoncargo.omm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.AirportWarehouseLocalChargeRate;
import com.dragoncargo.omm.model.service.AirportWarehouseLocalChargeRateService;
import com.dragoncargo.omm.service.model.AirportWarehouseLocalChargeRateData;
import com.dragoncargo.omm.service.model.AirportWarehouseLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.adapter.AirportWarehouseLocalChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.AirportWarehouseLocalChargeRateQueryCriteriaAdapter;

@Service("AirportWarehouseLocalChargeRateQueryService")
public class AirportWarehouseLocalChargeRateQueryServiceImpl extends
        AbstractChargeRateQueryServiceTemplate<AirportWarehouseLocalChargeRateQueryCriteria,
                AirportWarehouseLocalChargeRateData, AirportWarehouseLocalChargeRate,
                AirportWarehouseLocalChargeRateService>
{

    @Override
    protected AirportWarehouseLocalChargeRateData
            convertToChargeRateData(AirportWarehouseLocalChargeRate chargeRateConfiguration)
    {
        return new AirportWarehouseLocalChargeRateDataAdapter(chargeRateConfiguration);
    }

    @Override
    protected List<AirportWarehouseLocalChargeRate> filterByCriteria(
            List<AirportWarehouseLocalChargeRate> chargeRateConfigurationList,
            AirportWarehouseLocalChargeRateQueryCriteria criteria)
    {
        return chargeRateConfigurationList;
    }

    @Override
    protected AirportWarehouseLocalChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new AirportWarehouseLocalChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
