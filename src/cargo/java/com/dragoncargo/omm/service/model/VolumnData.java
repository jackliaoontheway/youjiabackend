package com.dragoncargo.omm.service.model;

import java.math.BigDecimal;

import com.dragoncargo.general.model.DimensionsUnit;

import lombok.Getter;
import lombok.Setter;

public class VolumnData
{

    /**
     * cm/m 立方米 立方厘米
     */
    private @Getter @Setter DimensionsUnit unit;

    private @Getter @Setter BigDecimal value;

}
