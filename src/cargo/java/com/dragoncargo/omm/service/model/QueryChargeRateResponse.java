package com.dragoncargo.omm.service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class QueryChargeRateResponse<T extends BaseChargeRateData>
{
    private @Setter @Getter boolean hasError;

    private @Setter @Getter String errorMsg;

    private @Setter @Getter List<T> chargeRateData;

}
