package com.dragoncargo.sales.service.model;

import com.dragoncargo.general.model.Volumn;
import com.dragoncargo.general.model.Weight;
import com.dragoncargo.omm.model.WeightSpec;
import com.polarj.model.GenericDbInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

public class BookingGoodsAttributeModify extends GenericDbInfo
{
    /**
     *
     */
    private static final long serialVersionUID = -267645140540175921L;

    /**
     * 品名 用,分隔
     */
    private @Setter @Getter String goodsName;

    /**
     * 外包装的数量
     */
    private @Setter @Getter Integer quantity;

    /**
     * 重量
     */
    private @Setter @Getter Weight weight;

    /**
     * 等级重量
     */
    private @Setter @Getter WeightSpec weightSpec;

    /**
     * 分泡比率 50%= 0.5 60%=0.6
     */
    private @Setter @Getter Double bulkyCargoDiscountRate;

    /**
     * 体积
     */
    private @Setter @Getter Volumn volume;
}
