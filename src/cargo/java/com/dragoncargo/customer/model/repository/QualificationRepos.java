package com.dragoncargo.customer.model.repository;

import com.dragoncargo.customer.model.QualificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QualificationRepos extends JpaRepository<QualificationInfo, Integer> {

}
