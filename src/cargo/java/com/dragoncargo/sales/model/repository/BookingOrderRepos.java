package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.BookingOrder;

public interface BookingOrderRepos extends JpaRepository<BookingOrder, Integer>
{
}
