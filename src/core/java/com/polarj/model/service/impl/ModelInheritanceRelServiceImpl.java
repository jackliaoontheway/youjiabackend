package com.polarj.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polarj.model.ModelInheritanceRel;
import com.polarj.model.repository.ModelInheritanceRelRepos;
import com.polarj.model.service.ModelInheritanceRelService;

@Service
public class ModelInheritanceRelServiceImpl extends EntityServiceImpl<ModelInheritanceRel, Integer>
        implements ModelInheritanceRelService
{

    @Override
    public List<ModelInheritanceRel> fetchModelInheritanceRelByBaseClassName(String baseClassName)
    {
        ModelInheritanceRelRepos repos = (ModelInheritanceRelRepos) getRepos();
        List<ModelInheritanceRel> res = repos.findByBaseClassName(baseClassName);
        return res;
    }

    @Override
    public ModelInheritanceRel fetchByUniqueData(String baseClassName, String subClassName)
    {
        ModelInheritanceRelRepos repos = (ModelInheritanceRelRepos) getRepos();
        ModelInheritanceRel res = repos.findByBaseClassNameAndSubClassName(baseClassName, subClassName);
        return res;
    }

}
