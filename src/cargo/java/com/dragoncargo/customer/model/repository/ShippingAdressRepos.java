package com.dragoncargo.customer.model.repository;

import com.dragoncargo.customer.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShippingAdressRepos extends JpaRepository<ShippingAddress, Integer> {

}
