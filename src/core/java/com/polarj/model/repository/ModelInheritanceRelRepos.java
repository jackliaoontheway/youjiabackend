package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.ModelInheritanceRel;

public interface ModelInheritanceRelRepos extends JpaRepository<ModelInheritanceRel, Integer>
{
    List<ModelInheritanceRel> findByBaseClassName(String baseClassName);

    ModelInheritanceRel findByBaseClassNameAndSubClassName(String baseClassName, String subClassName);
}
