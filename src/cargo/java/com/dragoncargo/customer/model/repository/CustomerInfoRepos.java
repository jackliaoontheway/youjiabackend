package com.dragoncargo.customer.model.repository;

import com.dragoncargo.customer.model.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerInfoRepos extends JpaRepository<CustomerInfo, Integer> {

}
