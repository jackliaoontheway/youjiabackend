package com.dragoncargo.customer.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.customer.model.CustomerLevel;

public interface CustomerLevelRepos extends JpaRepository<CustomerLevel, Integer>
{
}
