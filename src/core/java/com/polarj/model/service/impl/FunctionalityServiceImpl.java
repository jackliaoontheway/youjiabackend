package com.polarj.model.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.polarj.common.utility.FieldValueUtil;
import com.polarj.model.Functionality;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;
import com.polarj.model.enumeration.FunctionType;
import com.polarj.model.repository.FunctionalityRepos;
import com.polarj.model.service.FunctionalityService;

@Service
public class FunctionalityServiceImpl extends CachedEntityServiceImpl<Functionality> implements FunctionalityService
{
    @Override
    protected void setUniqueField()
    {
        Field[] uniqueFields = new Field[1];
        uniqueFields[0] = FieldValueUtil.getTheField(Functionality.class, "code", logger);
        if (uniqueFields[0] != null)
        {
            setUniqueFields(uniqueFields);
        }
    }

    @Override
    public Functionality fetchFunctionalityByCode(String code, String languageId)
    {
        return fetchFromCacheByUniqueValue(code, languageId);
    }

    @Override
    public List<Functionality> fetchFunctionalityByCodePattren(String codePattern, String languageId)
    {
        return fetchFromCacheByUniqueValuePattern(codePattern, languageId);
    }

    @Override
    public List<Functionality> fetchFunctionalityForModelClass(String modelName, String languageId)
    {
        FunctionalityRepos repos = (FunctionalityRepos) getRepos();
        List<Functionality> res =
                repos.findByTypeAndCodeLike(FunctionType.MODEL_CLASS_OPERATION.name(), modelName + "%");
        return replaceI18nFieldValueWithResource(res, languageId);
    }

    @Override
    public List<Functionality> fetchFunctionalityForModelObject(String modelName, String languageId)
    {
        FunctionalityRepos repos = (FunctionalityRepos) getRepos();
        List<Functionality> res =
                repos.findByTypeAndCodeLike(FunctionType.MODEL_INSTANCE_OPERATION.name(), modelName + "%");
        return replaceI18nFieldValueWithResource(res, languageId);
    }

    @Override
    public List<Functionality> listByOwner(UserAccount owner, String languageId)
    {
        if (owner == null)
        {
            return null;
        }

        if (owner.isSuperAdmin())
        {
            FunctionalityRepos repos = (FunctionalityRepos) getRepos();
            return replaceI18nFieldValueWithResource(repos.findAll(), languageId);
        }

        if (owner.getAllRoles() == null || owner.getAllRoles().size() == 0)
        {
            return null;
        }
        List<Functionality> records = new ArrayList<>();
        Set<Integer> fIdSet = new TreeSet<>();
        for (UserAccountRole role : owner.getAllRoles())
        {
            if (role.getFunctions() == null || role.getFunctions().size() == 0)
            {
                continue;
            }
            for (Functionality functionality : role.getFunctions())
            {
                if (!fIdSet.contains(functionality.getId()))
                {
                    fIdSet.add(functionality.getId());
                    records.add(functionality);
                }
            }
        }
        return replaceI18nFieldValueWithResource(records, languageId);
    }
}
