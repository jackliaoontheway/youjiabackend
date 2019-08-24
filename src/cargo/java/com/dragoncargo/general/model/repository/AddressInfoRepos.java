package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.AddressInfo;

public interface AddressInfoRepos extends JpaRepository<AddressInfo, Integer>
{
}
