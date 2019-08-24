package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.model.service.BookingOrderService;
import com.polarj.common.web.controller.UserRestrictionModelController;

@RestController
@RequestMapping("/bookingorders")
public class BookingOrderController
        extends UserRestrictionModelController<BookingOrder, Integer, BookingOrderService>
{
    public BookingOrderController()
    {
        super(BookingOrder.class);
    }

}
