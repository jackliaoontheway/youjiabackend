package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.sales.model.BookingExtractAndDeclare;
import com.dragoncargo.sales.model.service.BookingExtractAndDeclareService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/bookingextractanddeclares")
public class BookingExtractAndDeclareController
        extends ModelController<BookingExtractAndDeclare, Integer, BookingExtractAndDeclareService>
{
    public BookingExtractAndDeclareController()
    {
        super(BookingExtractAndDeclare.class);
    }
}
