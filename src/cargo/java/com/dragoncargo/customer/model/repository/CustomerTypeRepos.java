package com.dragoncargo.customer.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.customer.model.CustomerType;

public interface CustomerTypeRepos extends JpaRepository<CustomerType, Integer>
{
}
