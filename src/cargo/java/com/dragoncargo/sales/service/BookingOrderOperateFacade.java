package com.dragoncargo.sales.service;

import com.dragoncargo.sales.service.model.BookingGoodsAttributeModify;
import com.dragoncargo.sales.service.model.BookingOrderMainModify;
import com.dragoncargo.sales.service.model.BookingOrderOperateType;
import com.dragoncargo.sales.service.model.BookingQuotationItemModify;
import com.polarj.model.UserAccount;

public interface BookingOrderOperateFacade
{
    /**
     * 更新主体信息
     * @param orderId
     * @param data
     * @param type
     * @param userAcc
     * @param languageId
     * @return
     */
    Boolean modifyMainInfo(Integer orderId, BookingOrderMainModify data, BookingOrderOperateType type, UserAccount userAcc, String languageId);

    /**
     * 更新实际货物属性
     * @param orderId
     * @param data
     * @param type
     * @param userAcc
     * @param languageId
     * @return
     */
    Boolean modifyGoodsAttr(Integer orderId, BookingGoodsAttributeModify data, BookingOrderOperateType type, UserAccount userAcc, String languageId);

    /**
     * 更新费用明细
     * @param orderId
     * @param data
     * @param type
     * @param userAcc
     * @param languageId
     * @return
     */
    Boolean modifyChargeItem(Integer orderId, BookingQuotationItemModify data, BookingOrderOperateType type, UserAccount userAcc, String languageId);


}
