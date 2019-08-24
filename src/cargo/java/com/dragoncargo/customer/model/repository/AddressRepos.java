package com.dragoncargo.customer.model.repository;

import com.dragoncargo.customer.model.CustomerAddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepos extends JpaRepository<CustomerAddressInfo, Integer> {

}
