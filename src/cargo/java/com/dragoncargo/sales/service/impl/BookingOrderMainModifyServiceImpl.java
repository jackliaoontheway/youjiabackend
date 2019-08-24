package com.dragoncargo.sales.service.impl;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.service.model.BookingOrderMainModify;
import com.polarj.model.UserAccount;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service("BookingOrderMainModifyService")
public class BookingOrderMainModifyServiceImpl extends AbstractBookingOrderModifyService<BookingOrderMainModify>
{

    @Override
    protected Boolean create(BookingOrder bookingOrder, BookingOrderMainModify data, UserAccount userAcc, String languageId) {
        return null;
    }

    @Override
    protected Boolean update(BookingOrder bookingOrder, BookingOrderMainModify data, UserAccount userAcc, String languageId) {
        bookingOrder.setMawb(data.getMawb());
        bookingOrder.setHawb(data.getHawb());
        bookingOrder.setPickupLocationCode(data.getPickupLocationCode());
        bookingOrder.setReceivingCityCode(data.getReceivingCityCode());
        bookingOrder.setTransferNeeded(data.getTransferNeeded());
        bookingOrderService.update(bookingOrder.getId(),bookingOrder,userAcc.getId(),languageId);
        return true;
    }

    @Override
    protected Boolean read(BookingOrder bookingOrder, BookingOrderMainModify data, UserAccount userAcc, String languageId) {
        return null;
    }

    @Override
    protected Boolean delete(BookingOrder bookingOrder, BookingOrderMainModify data, UserAccount userAcc, String languageId) {
        return null;
    }
}
