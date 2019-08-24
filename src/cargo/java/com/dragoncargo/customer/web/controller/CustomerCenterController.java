package com.dragoncargo.customer.web.controller;

import com.dragoncargo.customer.model.CustomerCenter;
import com.dragoncargo.customer.model.service.CustomerCenterService;
import com.polarj.common.web.controller.UserRestrictionModelController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customercenters")
public class CustomerCenterController
		extends UserRestrictionModelController<CustomerCenter, Integer, CustomerCenterService> {

	public CustomerCenterController() {
		super(CustomerCenter.class);
	}

}
