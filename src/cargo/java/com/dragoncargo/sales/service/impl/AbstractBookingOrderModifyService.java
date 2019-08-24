package com.dragoncargo.sales.service.impl;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.model.service.BookingOrderService;
import com.dragoncargo.sales.service.BookingOrderModifyService;
import com.dragoncargo.sales.service.model.BookingOrderOperateType;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBookingOrderModifyService<T extends GenericDbInfo> implements BookingOrderModifyService<T>
{

    @Autowired
    protected BookingOrderService bookingOrderService;

    protected abstract Boolean create(BookingOrder bookingOrder,T data,UserAccount userAcc, String languageId);

    protected abstract Boolean update(BookingOrder bookingOrder,T data,UserAccount userAcc, String languageId);

    protected abstract Boolean read(BookingOrder bookingOrder,T data,UserAccount userAcc, String languageId);

    protected abstract Boolean delete(BookingOrder bookingOrder,T data,UserAccount userAcc, String languageId);

    @Override
    public Boolean select2Operate(Integer orderId, T data, BookingOrderOperateType type, UserAccount userAcc, String languageId) {
        BookingOrder bookingOrder =  bookingOrderService.getById(orderId,languageId);
        if(type.equals(BookingOrderOperateType.CREATE)){
            this.create(bookingOrder,data,userAcc,languageId);
        }else if(type.equals(BookingOrderOperateType.READ)){
            this.read(bookingOrder,data,userAcc,languageId);
        }else if(type.equals(BookingOrderOperateType.UPDATE)){
            this.update(bookingOrder,data,userAcc,languageId);
        }else if(type.equals(BookingOrderOperateType.DELETE)){
            this.delete(bookingOrder,data,userAcc,languageId);
        }
        return true;
    }

}
