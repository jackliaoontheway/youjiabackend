package com.dragoncargo.omm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.AirportLocalChargeRate;
import com.dragoncargo.omm.model.service.AirportLocalChargeRateService;
import com.dragoncargo.omm.service.model.AirportLocalChargeRateData;
import com.dragoncargo.omm.service.model.AirportLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.adapter.AirportLocalChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.AirportLocalChargeRateQueryCriteriaAdapter;

@Service("AirportLocalChargeRateQueryService")
public class AirportLocalChargeRateQueryServiceImpl
        extends AbstractChargeRateQueryServiceTemplate<AirportLocalChargeRateQueryCriteria, AirportLocalChargeRateData,
                AirportLocalChargeRate, AirportLocalChargeRateService>
{

    @Override
    protected AirportLocalChargeRateData convertToChargeRateData(AirportLocalChargeRate airportLocalChargeRate)
    {
        return new AirportLocalChargeRateDataAdapter(airportLocalChargeRate);
    }

    @Override
    protected List<AirportLocalChargeRate> filterByCriteria(List<AirportLocalChargeRate> chargeRateConfigurationList,
            AirportLocalChargeRateQueryCriteria criteria)
    {
        return chargeRateConfigurationList;
    }

    @Override
    protected AirportLocalChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new AirportLocalChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
