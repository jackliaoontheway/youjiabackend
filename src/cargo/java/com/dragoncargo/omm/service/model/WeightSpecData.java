package com.dragoncargo.omm.service.model;

import com.dragoncargo.omm.model.WeightSpec;

import lombok.Getter;
import lombok.Setter;

public class WeightSpecData
{

    /**
     * 45+ 100+ 300+ 500+ 1000+ 2000+ 3000+
     */
    private @Setter @Getter String code;

    private @Setter @Getter String name;

    /**
     * 
     * Minimum <= value < Maximum
     */
    private @Setter @Getter WeightData minimumRangeWeight;

    private @Setter @Getter WeightData maximumRangeWeight;

    
    public WeightSpecData(WeightSpec weightSpec)
    {

        if (weightSpec == null)
        {
            return;
        }

        this.code = weightSpec.getCode();
        this.name = weightSpec.getName();
        if (weightSpec.getMaximumRangeWeight() != null)
        {
            this.maximumRangeWeight = new WeightData();
            this.maximumRangeWeight.setUnit(weightSpec.getMaximumRangeWeight().getUnit());
            this.maximumRangeWeight.setValue(weightSpec.getMaximumRangeWeight().getValue());
        }

        if (weightSpec.getMinimumRangeWeight() != null)
        {
            this.minimumRangeWeight = new WeightData();
            this.minimumRangeWeight.setUnit(weightSpec.getMinimumRangeWeight().getUnit());
            this.minimumRangeWeight.setValue(weightSpec.getMaximumRangeWeight().getValue());
        }
    }
    
}
