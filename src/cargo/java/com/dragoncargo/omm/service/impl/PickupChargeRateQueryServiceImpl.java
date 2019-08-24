package com.dragoncargo.omm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.PickupChargeRate;
import com.dragoncargo.omm.model.PickupLocation;
import com.dragoncargo.omm.model.service.PickupChargeRateService;
import com.dragoncargo.omm.service.model.PickupChargeRateData;
import com.dragoncargo.omm.service.model.PickupChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.adapter.PickupChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.PickupChargeRateQueryCriteriaAdapter;

@Service("PickupChargeRateQueryService")
public class PickupChargeRateQueryServiceImpl extends AbstractChargeRateQueryServiceTemplate<
        PickupChargeRateQueryCriteria, PickupChargeRateData, PickupChargeRate, PickupChargeRateService>
{

    @Override
    protected PickupChargeRateData convertToChargeRateData(PickupChargeRate pickupChargeRate)
    {
        return new PickupChargeRateDataAdapter(pickupChargeRate);
    }

    @Override
    protected List<PickupChargeRate> filterByCriteria(List<PickupChargeRate> chargeRateConfigurationList,
            PickupChargeRateQueryCriteria criteria)
    {
        List<PickupChargeRate> resultList = new ArrayList<>();
        for (PickupChargeRate chargeRate : chargeRateConfigurationList)
        {
            List<PickupLocation> pickupLocationList = chargeRate.getPickupLocationList();
            for (PickupLocation pickupLocation : pickupLocationList)
            {
                if (criteria.getPickupLocation() != null
                        && criteria.getPickupLocation().equals(pickupLocation.getCode()))
                {
                    resultList.add(chargeRate);
                }
            }
        }

        return resultList;
    }

    @Override
    protected PickupChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new PickupChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
