package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

public class TransferChargeRateQueryCriteria extends BaseCriteria
{
    
    private @Setter @Getter String aviationCompanyCode;

    private @Setter @Getter String arrivingAirPortCode;
    
    private @Setter @Getter String transferToCityCode;
}
