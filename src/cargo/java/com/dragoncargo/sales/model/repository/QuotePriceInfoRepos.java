package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.QuotePriceInfo;

public interface QuotePriceInfoRepos extends JpaRepository<QuotePriceInfo, Integer>
{
}
