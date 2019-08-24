package com.polarj.common.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.HasOwner;
import com.polarj.model.UserAccount;
import com.polarj.model.service.EntityService;

public abstract class UserRestrictionModelController<M extends GenericDbInfo & HasOwner, ID extends Serializable, S extends EntityService<M, ID>>
        extends ModelController<M, ID, S>
{

    public UserRestrictionModelController(Class<M> clazz)
    {
        super(clazz);
    }

    protected M getUserEntityById(ID id, Integer operId, String languageId)
    {
        M m = null;
        m = service.getById(id, languageId);
        if (m != null && m.getOwner() != null && m.getOwner().getId().equals(operId))
        {
            return m;
        }
        return null;
    }

    protected M updateUserEntity(ID id, M entityWithUpdatedData, Integer operId, String languageId)
    {
        M m = null;
        m = service.getById(id, languageId);
        if (m != null && m.getOwner() != null && m.getOwner().getId().equals(operId))
        {
            m = service.update(id, entityWithUpdatedData, operId, languageId);
            return m;
        }
        return m;
    }

    protected boolean removeUserEntity(ID id, Integer operId, String languageId)
    {
        M m = service.getById(id, languageId);
        if (m != null && m.getOwner() != null && m.getOwner().getId().equals(operId))
        {
            service.delete(id, operId);
            return true;
        }
        return false;
    }

    protected Page<M> searchUserEntities(M searchCriteria, String sortField, boolean sortDesc, Integer operId,
            String languageId, Integer dataLevel)
    {
        Page<M> res = null;
        UserAccount owner = new UserAccount();
        owner.setId(operId);
        if (searchCriteria != null)
        {
            searchCriteria.setOwner(owner);
        }
        res = service.listByCriteria(searchCriteria, sortField, sortDesc, languageId, dataLevel);
        return res;
    }

    protected List<M> listUserEntities(List<ID> ids, Integer operId, String languageId)
    {
        List<M> res = service.list(ids, languageId);
        if (res != null && res.size() > 0)
        {
            List<M> euacsForUser = new ArrayList<M>();
            for (M euac : res)
            {
                if (euac.getOwner() != null && euac.getOwner().getId().equals(operId))
                {
                    euacsForUser.add(euac);
                }
            }
            return euacsForUser;
        }
        return null;
    }

    protected void addOwnerFor(M model, UserAccount ua)
    {
        // FIXME: 应该使用登录用户的主帐号作为owner。
        if (model.getOwner() != null && model.getOwner().getId() != null)
        {
            return;
        }
        UserAccount u = new UserAccount();
        u.setId(ua.getId());
        model.setOwner(u);
    }
}
