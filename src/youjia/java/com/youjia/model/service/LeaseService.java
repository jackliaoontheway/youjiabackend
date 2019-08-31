package com.youjia.model.service;

import com.polarj.model.service.EntityService;

import com.youjia.model.Lease;
import com.youjia.model.Renter;

public interface LeaseService extends EntityService<Lease, Integer>
{

	Lease findByRenter(Renter renter);

	boolean withdrawRequest(Renter renter);
	
	boolean withdrawConfirm(Renter renter);
}
