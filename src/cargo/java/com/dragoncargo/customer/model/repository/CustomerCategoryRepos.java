package com.dragoncargo.customer.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.customer.model.CustomerCategory;

public interface CustomerCategoryRepos extends JpaRepository<CustomerCategory, Integer>
{
}
