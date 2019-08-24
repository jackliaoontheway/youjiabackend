package com.dragoncargo.omm.service.model;

import java.math.BigDecimal;

import com.dragoncargo.general.model.FeeInfo;

import lombok.Getter;
import lombok.Setter;

public class FeeData
{

    private @Getter @Setter String currency;

    private @Getter @Setter BigDecimal amount;

    public FeeData(FeeInfo feeinfo)
    {
        if(feeinfo != null) {
            this.currency = feeinfo.getCurrency();
            this.amount = feeinfo.getAmount();
        }
    }

    public FeeData()
    {

    }

}
