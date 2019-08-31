package com.youjia.model.service;

import com.polarj.model.service.EntityService;

import com.youjia.model.RentBill;
import com.youjia.model.Renter;

public interface RentBillService extends EntityService<RentBill, Integer>
{

	RentBill findByRenter(Renter renter);
}
