package com.dragoncargo.omm.service.model;

import java.math.BigDecimal;

import com.dragoncargo.general.model.WeightUnit;

import lombok.Getter;
import lombok.Setter;

public class WeightData
{
    private @Getter @Setter WeightUnit unit;

    private @Getter @Setter BigDecimal value;
    
}
