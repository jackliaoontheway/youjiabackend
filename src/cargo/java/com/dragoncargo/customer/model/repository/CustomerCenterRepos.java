package com.dragoncargo.customer.model.repository;

import com.dragoncargo.customer.model.CustomerCenter;
import com.polarj.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomerCenterRepos extends JpaRepository<CustomerCenter, Integer> {

    List<CustomerCenter> findByOwner(UserAccount owner);
}
