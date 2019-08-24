package com.dragoncargo.general.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class Dimensions
{
    private @Getter @Setter DimensionsUnit unit;

    private @Getter @Setter BigDecimal length;

    private @Getter @Setter BigDecimal width;

    private @Getter @Setter BigDecimal height;

}
