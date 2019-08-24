package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.QuoteFreightFee;

public interface QuoteFreightFeeRepos extends JpaRepository<QuoteFreightFee, Integer>
{
}
