package com.dragoncargo.customer.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.customer.model.CustomerStatus;

public interface CustomerStatusRepos extends JpaRepository<CustomerStatus, Integer>
{
}
