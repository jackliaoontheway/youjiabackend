package com.polarj.model.service.impl;


import com.polarj.model.ModelFilterStrategy;
import com.polarj.model.repository.ModelFilterStrategyRepos;
import com.polarj.model.service.ModelFilterStrategyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelFilterStrategyServiceImpl extends EntityServiceImpl<ModelFilterStrategy, Integer>
        implements ModelFilterStrategyService
{
    @Override
    public List<ModelFilterStrategy> findByClassFullName(String classFullName, String languageId){
        ModelFilterStrategyRepos repos = (ModelFilterStrategyRepos) getRepos();
        List<ModelFilterStrategy>  list = repos.findByClassFullName(classFullName);
        return replaceI18nFieldValueWithResource(dataClone(list), languageId);
    }
}
