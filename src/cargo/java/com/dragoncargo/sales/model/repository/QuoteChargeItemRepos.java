package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.QuoteChargeItem;

public interface QuoteChargeItemRepos extends JpaRepository<QuoteChargeItem, Integer>
{
}
