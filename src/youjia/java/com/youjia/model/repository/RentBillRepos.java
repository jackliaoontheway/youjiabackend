package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.RentBill;
import com.youjia.model.Renter;

public interface RentBillRepos extends JpaRepository<RentBill, Integer>
{

	RentBill findFirstByRenter(Renter renter);
}
