package com.dragoncargo.customer.model.repository;


import com.dragoncargo.customer.model.InvoiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvoiceInfoRepos extends JpaRepository<InvoiceInfo, Integer> {

}
