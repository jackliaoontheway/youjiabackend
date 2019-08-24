package com.dragoncargo.omm.model.service;

import java.util.List;

import com.dragoncargo.omm.model.AbstractChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.polarj.model.service.EntityService;

public interface AbstractChargeRateService<M extends AbstractChargeRate> extends EntityService<M, Integer>
{

    List<M> findByChargeRateItem(ChargeRateItem item);

}
