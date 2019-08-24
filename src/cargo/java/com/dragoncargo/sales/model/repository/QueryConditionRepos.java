package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.QueryCondition;

public interface QueryConditionRepos extends JpaRepository<QueryCondition, Integer>
{
}
