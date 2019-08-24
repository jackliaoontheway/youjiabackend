package com.dragoncargo.omm.model.service.impl;

import com.dragoncargo.omm.model.AbstractChargeRate;
import com.dragoncargo.omm.model.service.AbstractChargeRateService;
import com.polarj.model.service.impl.EntityServiceImpl;

public abstract class AbstractChargeRateServiceImpl<T extends AbstractChargeRate> extends EntityServiceImpl<T, Integer>
        implements AbstractChargeRateService<T>
{

}
