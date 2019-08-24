package com.polarj.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.polarj.model.Functionality;

public interface FunctionalityService extends EntityService<Functionality, Integer>
{
    @Transactional
    public Functionality fetchFunctionalityByCode(String code, String languageId);

    @Transactional
    public List<Functionality> fetchFunctionalityByCodePattren(String codePattern, String languageId);

    @Transactional
    public List<Functionality> fetchFunctionalityForModelClass(String modelName, String languageId);

    @Transactional
    public List<Functionality> fetchFunctionalityForModelObject(String modelName, String languageId);
    
}
