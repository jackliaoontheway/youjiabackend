package com.polarj.model.service;

import com.polarj.model.ModelFilterStrategy;

import java.util.List;

public interface ModelFilterStrategyService extends EntityService<ModelFilterStrategy, Integer>
{
    List<ModelFilterStrategy> findByClassFullName(String classFullName,String languageId);
}
