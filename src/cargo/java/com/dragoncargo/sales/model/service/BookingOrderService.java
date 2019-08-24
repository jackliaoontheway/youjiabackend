package com.dragoncargo.sales.model.service;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.service.model.*;
import com.polarj.model.UserAccount;
import com.polarj.model.service.EntityService;

public interface BookingOrderService extends EntityService<BookingOrder, Integer>
{

    BookingOrder generateBookingOrder(BookingOrder bookingOrder, UserAccount userAcc, String languageId);

    BookingOrder cloneBookingOrder(BookingOrder bookingOrder, UserAccount userAcc, String languageId);

}
