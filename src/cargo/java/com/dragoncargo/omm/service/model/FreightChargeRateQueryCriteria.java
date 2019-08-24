package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

public class FreightChargeRateQueryCriteria extends BaseCriteria
{
    private @Setter @Getter WeightData weight;

    private @Setter @Getter DimensionsData dimensions;

    private @Setter @Getter VolumnData volumnData;
    
    private @Setter @Getter boolean queryAdjacentRange;

}
