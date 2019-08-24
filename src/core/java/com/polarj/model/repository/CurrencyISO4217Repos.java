package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.CurrencyISO4217;

public interface CurrencyISO4217Repos extends JpaRepository<CurrencyISO4217, Integer>
{
    public CurrencyISO4217 findFirstByCode(String code);
}
