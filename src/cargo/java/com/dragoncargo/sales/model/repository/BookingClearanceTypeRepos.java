package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.BookingCustomsClearanceType;

public interface BookingClearanceTypeRepos extends JpaRepository<BookingCustomsClearanceType, Integer>
{
}
