package com.dragoncargo.sales.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.sales.model.BookingExtractAndDeclare;
import com.dragoncargo.sales.model.service.BookingExtractAndDeclareService;

@Service
public class BookingExtractAndDeclareServiceImpl extends EntityServiceImpl<BookingExtractAndDeclare, Integer>
        implements BookingExtractAndDeclareService
{
}
