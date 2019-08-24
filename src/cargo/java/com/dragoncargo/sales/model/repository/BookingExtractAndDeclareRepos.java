package com.dragoncargo.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.sales.model.BookingExtractAndDeclare;

public interface BookingExtractAndDeclareRepos extends JpaRepository<BookingExtractAndDeclare, Integer>
{
}
