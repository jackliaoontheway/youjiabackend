package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.general.model.AviationCompany;
import com.dragoncargo.omm.model.AviationCompanyLocalChargeRate;
import com.dragoncargo.omm.service.model.AviationCompanyLocalChargeRateData;

public class AviationCompanyLocalChargeRateDataAdapter extends AviationCompanyLocalChargeRateData
{

    public AviationCompanyLocalChargeRateDataAdapter(AviationCompanyLocalChargeRate chargeRateConfiguration)
    {
        AviationCompany aviationCompany = chargeRateConfiguration.getAviationCompany();

        if (aviationCompany != null)
        {
            this.setAviationCompanyCode(aviationCompany.getCode());
            this.setAviationCompanyName(aviationCompany.getName());
        }
    }

}
