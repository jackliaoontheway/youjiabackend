package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Lease;

public interface LeaseRepos extends JpaRepository<Lease, Integer>
{
}
