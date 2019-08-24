package com.dragoncargo.sales.service.impl;

import com.dragoncargo.sales.model.BookingGoodsAttribute;
import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.service.model.BookingGoodsAttributeModify;
import com.polarj.model.UserAccount;
import org.springframework.stereotype.Service;


@Service("BookingGoodsAttributeModifyService")
public class BookingGoodsAttributeModifyServiceImpl extends AbstractBookingOrderModifyService<BookingGoodsAttributeModify>
{


    @Override
    protected Boolean create(BookingOrder bookingOrder, BookingGoodsAttributeModify data, UserAccount userAcc, String languageId) {
        BookingGoodsAttribute bookingGoodsAttribute = new BookingGoodsAttribute();
        bookingGoodsAttribute.setGoodsName(data.getGoodsName());
        bookingGoodsAttribute.setWeight(data.getWeight());
        bookingGoodsAttribute.setWeightSpec(data.getWeightSpec());
        bookingGoodsAttribute.setQuantity(data.getQuantity());
        bookingGoodsAttribute.setVolume(data.getVolume());
        bookingGoodsAttribute.setBulkyCargoDiscountRate(data.getBulkyCargoDiscountRate());
        bookingOrder.setActualGoogsAttribute(bookingGoodsAttribute);
        bookingOrderService.update(bookingOrder.getId(),bookingOrder,userAcc.getId(),languageId);
        return true;
    }

    @Override
    protected Boolean update(BookingOrder bookingOrder, BookingGoodsAttributeModify data, UserAccount userAcc, String languageId) {
        BookingGoodsAttribute bookingGoodsAttribute =  bookingOrder.getActualGoogsAttribute();
        bookingGoodsAttribute.setGoodsName(data.getGoodsName());
        bookingGoodsAttribute.setWeight(data.getWeight());
        bookingGoodsAttribute.setWeightSpec(data.getWeightSpec());
        bookingGoodsAttribute.setQuantity(data.getQuantity());
        bookingGoodsAttribute.setVolume(data.getVolume());
        bookingGoodsAttribute.setBulkyCargoDiscountRate(data.getBulkyCargoDiscountRate());
        bookingOrder.setActualGoogsAttribute(bookingGoodsAttribute);
        bookingOrderService.update(bookingOrder.getId(),bookingOrder,userAcc.getId(),languageId);
        return true;
    }

    @Override
    protected Boolean read(BookingOrder bookingOrder, BookingGoodsAttributeModify data, UserAccount userAcc, String languageId) {
        return null;
    }

    @Override
    protected Boolean delete(BookingOrder bookingOrder, BookingGoodsAttributeModify data, UserAccount userAcc, String languageId) {
        return null;
    }
}
