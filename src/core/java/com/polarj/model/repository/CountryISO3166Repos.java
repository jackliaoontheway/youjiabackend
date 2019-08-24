package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.CountryISO3166;

public interface CountryISO3166Repos extends JpaRepository<CountryISO3166, Integer>
{
    public CountryISO3166 findFirstByCode(String code);
}
