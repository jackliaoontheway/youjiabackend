package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.BookingDeliveryType;

public interface BookingDeliveryTypeRepos extends JpaRepository<BookingDeliveryType, Integer>
{
}
