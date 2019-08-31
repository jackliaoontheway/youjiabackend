package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.RentBill;
import com.youjia.model.Renter;
import com.youjia.model.repository.RentBillRepos;
import com.youjia.model.service.RentBillService;

@Service
public class RentBillServiceImpl extends EntityServiceImpl<RentBill, Integer> implements RentBillService {

	@Override
	public RentBill findByRenter(Renter renter) {
		RentBillRepos repos = (RentBillRepos) this.getRepos();
		return repos.findFirstByRenterOrderByIdDesc(renter);
	}
}
