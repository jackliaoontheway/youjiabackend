package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Lease;
import com.youjia.model.Renter;

public interface LeaseRepos extends JpaRepository<Lease, Integer>
{

	Lease findFirstByRenter(Renter renter);
}
