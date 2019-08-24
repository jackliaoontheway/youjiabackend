package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.sales.model.BookingDeliveryType;
import com.dragoncargo.sales.model.service.BookingDeliveryTypeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/bookingdeliverytypes")
public class BookingDeliveryTypeController
        extends ModelController<BookingDeliveryType, Integer, BookingDeliveryTypeService>
{
    public BookingDeliveryTypeController()
    {
        super(BookingDeliveryType.class);
    }
}
