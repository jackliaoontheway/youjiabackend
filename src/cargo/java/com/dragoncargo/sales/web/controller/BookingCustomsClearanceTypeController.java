package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.sales.model.BookingCustomsClearanceType;
import com.dragoncargo.sales.model.service.BookingCustomsClearanceTypeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/bookingcustomsclearancetypes")
public class BookingCustomsClearanceTypeController
        extends ModelController<BookingCustomsClearanceType, Integer, BookingCustomsClearanceTypeService>
{
    public BookingCustomsClearanceTypeController()
    {
        super(BookingCustomsClearanceType.class);
    }
}
