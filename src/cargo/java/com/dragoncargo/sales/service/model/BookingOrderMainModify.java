package com.dragoncargo.sales.service.model;

import com.polarj.model.GenericDbInfo;
import lombok.Getter;
import lombok.Setter;

public class BookingOrderMainModify extends GenericDbInfo
{

    /**
     * 主单号
     */
    private @Getter @Setter String mawb;

    /**
     * 分单号
     */
    private @Getter @Setter String hawb;

    /**
     * 提货点
     */
    private @Setter @Getter String pickupLocationCode;

    /**
     * 收货地城市代码
     */
    private @Setter @Getter String receivingCityCode;

    /**
     * 是否需要转运
     */
    private @Setter @Getter Boolean transferNeeded;

}
