package com.dragoncargo.sales.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.sales.model.BookingDeliveryType;
import com.dragoncargo.sales.model.service.BookingDeliveryTypeService;

@Service
public class BookingDeliveryTypeServiceImpl extends EntityServiceImpl<BookingDeliveryType, Integer>
        implements BookingDeliveryTypeService
{
}
