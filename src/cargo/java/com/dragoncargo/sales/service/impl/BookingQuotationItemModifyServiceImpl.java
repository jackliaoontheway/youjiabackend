package com.dragoncargo.sales.service.impl;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.model.BookingQuotationItem;
import com.polarj.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("BookingQuotationItemModifyService")
public class BookingQuotationItemModifyServiceImpl extends AbstractBookingOrderModifyService<BookingQuotationItem>
{

    @Override
    protected Boolean create(BookingOrder bookingOrder, BookingQuotationItem data, UserAccount userAcc, String languageId) {
        List<BookingQuotationItem> itemList = bookingOrder.getActualQuotations();
        BookingQuotationItem item = new BookingQuotationItem();
        item.setAirPortCode(data.getAirPortCode());
        item.setChargeName(data.getChargeName());
        item.setChargeUnit(data.getChargeUnit());
        item.setUnitPrice(data.getUnitPrice());
        item.setWeightType(data.getWeightType());
        item.setMinimumCharge(data.getMinimumCharge());
        item.setMaximumCharge(data.getMaximumCharge());
        item.setChargeRateType(data.getChargeRateType());
        itemList.add(item);
        bookingOrder.setActualQuotations(itemList);
        bookingOrderService.update(bookingOrder.getId(),bookingOrder,userAcc.getId(),languageId);

        return true;
    }

    @Override
    protected Boolean update(BookingOrder bookingOrder, BookingQuotationItem data, UserAccount userAcc, String languageId) {
        List<BookingQuotationItem> itemList = bookingOrder.getActualQuotations();
        for(BookingQuotationItem item: itemList){
            if(item.getId().equals(data.getId())){
                item.setAirPortCode(data.getAirPortCode());
                item.setChargeName(data.getChargeName());
                item.setChargeUnit(data.getChargeUnit());
                item.setUnitPrice(data.getUnitPrice());
                item.setWeightType(data.getWeightType());
                item.setMinimumCharge(data.getMinimumCharge());
                item.setMaximumCharge(data.getMaximumCharge());
                item.setChargeRateType(data.getChargeRateType());
            }
        }
        bookingOrder.setActualQuotations(itemList);
        bookingOrderService.update(bookingOrder.getId(),bookingOrder,userAcc.getId(),languageId);

        return true;
    }

    @Override
    protected Boolean read(BookingOrder bookingOrder, BookingQuotationItem data, UserAccount userAcc, String languageId) {
        return null;
    }

    @Override
    protected Boolean delete(BookingOrder bookingOrder, BookingQuotationItem data, UserAccount userAcc, String languageId) {
        List<BookingQuotationItem> itemList = bookingOrder.getActualQuotations();
        itemList = itemList.stream().filter(e->!e.getId().equals(data.getId())).collect(Collectors.toList());
        bookingOrder.setActualQuotations(itemList);
        bookingOrderService.update(bookingOrder.getId(),bookingOrder,userAcc.getId(),languageId);
        return true;
    }
}
