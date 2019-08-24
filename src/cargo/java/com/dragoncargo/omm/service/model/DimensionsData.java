package com.dragoncargo.omm.service.model;

import java.math.BigDecimal;

import com.dragoncargo.general.model.DimensionsUnit;

import lombok.Getter;
import lombok.Setter;

public class DimensionsData
{
    private @Getter @Setter DimensionsUnit unit;

    private @Getter @Setter BigDecimal length;

    private @Getter @Setter BigDecimal width;

    private @Getter @Setter BigDecimal height;

}
