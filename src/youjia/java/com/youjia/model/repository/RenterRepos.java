package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Renter;

public interface RenterRepos extends JpaRepository<Renter, Integer>
{
}
