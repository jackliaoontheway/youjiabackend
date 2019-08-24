package com.dragoncargo.sales.service;

import com.dragoncargo.sales.service.model.BookingOrderOperateType;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.UserAccount;

public interface BookingOrderModifyService<T extends GenericDbInfo>
{

    Boolean select2Operate(Integer orderId, T data, BookingOrderOperateType type, UserAccount userAcc, String languageId);

}
