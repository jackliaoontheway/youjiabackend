package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.AviationCompany;

public interface AviationCompanyRepos extends JpaRepository<AviationCompany, Integer>
{
    AviationCompany findByCode(String code);
}
