package com.polarj.model.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.model.Functionality;
import com.polarj.model.FunctionalityModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;
import com.polarj.model.repository.UserAccountRoleRepos;
import com.polarj.model.service.FunctionalityService;
import com.polarj.model.service.UserAccountRoleService;

@Service
public class UserAccountRoleServiceImpl extends EntityServiceImpl<UserAccountRole, Integer>
        implements UserAccountRoleService
{

    private final static int MAX_FUNCTIONALITY_LEVEL = 3;

    @Autowired
    private FunctionalityService functionalityService;

    @Override
    public UserAccountRole fetchByRoleCode(String userRoleCode)
    {
        UserAccountRoleRepos repos = (UserAccountRoleRepos) getRepos();
        UserAccountRole userRole = repos.findByCode(userRoleCode);
        return dataClone(userRole);
    }

    @Override
    public UserAccountRole fetchDefaultRole()
    {
        UserAccountRoleRepos repos = (UserAccountRoleRepos) getRepos();
        UserAccountRole userRole = repos.findByDefaultRole(true);
        return dataClone(userRole);
    }

    @Override
    public List<UserAccountRole> listByOwner(UserAccount owner, String languageId)
    {
        UserAccountRoleRepos repos = (UserAccountRoleRepos) getRepos();
        return repos.findByOwner(owner);
    }

    @Override
    public List<FunctionalityModelView> fetchFunctionalitiesByUserRole(UserAccount userAcc, Integer userRoleId,
            String languageId)
    {
        // 查询登录帐号拥有哪些权限
        List<Functionality> userAccFunctionilities = functionalityService.listByOwner(userAcc, languageId);

        if (userAccFunctionilities == null || userAccFunctionilities.size() == 0)
        {
            return null;
        }

        // 通过 userRoleId 查询 这个角色拥有哪些权限
        UserAccountRole userAccRole = this.getById(userRoleId, 2, languageId);

        List<Functionality> roleFunctionilities = userAccRole.getFunctions();
        Set<Integer> roleFunctionilitiesSet = new TreeSet<>();
        if (roleFunctionilities != null && roleFunctionilities.size() > 0)
        {
            for (Functionality functionality : roleFunctionilities)
            {
                roleFunctionilitiesSet.add(functionality.getId());
            }
        }

        for (Functionality functionality : userAccFunctionilities)
        {
            functionality.setSelected(false);
            if (roleFunctionilitiesSet.contains(functionality.getId()))
            {
                functionality.setSelected(true);
            }
        }

        return groupByMenu(userAccFunctionilities);
    }

    private List<FunctionalityModelView> groupByMenu(List<Functionality> userAccFunctionilities)
    {
        List<FunctionalityModelView> firstMenus = new ArrayList<>();
        for (Functionality functionality : userAccFunctionilities)
        {
            if (functionality.getParentMenu() == null && "MENU".equals(functionality.getType()))
            {
                FunctionalityModelView menu = buildFunctionalityModelView(functionality);
                // 在这里递归 找到 子菜单/子功能 目前设置3层
                menu.setSubMenus(findSubMenus(menu, userAccFunctionilities, 1));

                firstMenus.add(menu);
            }
        }

        sortFunctionalityByPositionSn(firstMenus);
        return firstMenus;
    }

    private FunctionalityModelView buildFunctionalityModelView(Functionality functionality)
    {
        FunctionalityModelView modelView = new FunctionalityModelView();
        modelView.setId(functionality.getId());
        modelView.setPositionSn(functionality.getPositionSn());
        modelView.setLabel(functionality.getLabel());
        modelView.setSelected(functionality.isSelected());
        return modelView;
    }

    private void sortFunctionalityByPositionSn(List<FunctionalityModelView> list)
    {
        list.sort(new Comparator<FunctionalityModelView>()
        {
            @Override
            public int compare(FunctionalityModelView o1, FunctionalityModelView o2)
            {
                Integer firstSn = (o1.getPositionSn() == null ? 0 : o1.getPositionSn());
                Integer secondSn = (o2.getPositionSn() == null ? 0 : o2.getPositionSn());
                return firstSn.compareTo(secondSn);
            }
        });
    }

    private List<FunctionalityModelView> findSubMenus(FunctionalityModelView parentMenu,
            List<Functionality> userAccFunctionilities, int i)
    {
        if (i++ == MAX_FUNCTIONALITY_LEVEL)
        {
            return null;
        }

        List<FunctionalityModelView> subMenus = new ArrayList<>();
        for (Functionality menu : userAccFunctionilities)
        {
            if (menu.getParentMenu() != null && parentMenu.getId().equals(menu.getParentMenu().getId()))
            {
                FunctionalityModelView subMenu = buildFunctionalityModelView(menu);
                subMenu.setSubMenus(findSubMenus(subMenu, userAccFunctionilities, i));
                subMenus.add(subMenu);
            }
        }
        sortFunctionalityByPositionSn(subMenus);
        return subMenus;
    }

    @Override
    public Boolean grantFunctionalityForUserRole(List<FunctionalityModelView> functionalities, Integer operatorId,
            String languageId)
    {
        if (functionalities == null || functionalities.size() == 0)
        {
            return false;
        }
        Integer userRolrId = functionalities.get(0).getUserRoleId();
        UserAccountRole userAccRole = this.getById(userRolrId, languageId);
        List<Functionality> roleFunctionilities = userAccRole.getFunctions();
        Set<Integer> roleFunctionilitiesSet = new TreeSet<>();
        if (roleFunctionilities != null && roleFunctionilities.size() > 0)
        {
            for (Functionality functionality : roleFunctionilities)
            {
                roleFunctionilitiesSet.add(functionality.getId());
            }
        } else {
            roleFunctionilities = new ArrayList<>();
            userAccRole.setFunctions(roleFunctionilities);
        }
        
        Set<Integer> removeFunctionilitiesSet = new TreeSet<>();
        for (FunctionalityModelView functionality : functionalities)
        {
            if (functionality.isSelected())
            {
                if(!roleFunctionilitiesSet.contains(functionality.getId())) {
                    Functionality newFunctionality = functionalityService.getById(functionality.getId(), languageId);
                    roleFunctionilities.add(newFunctionality);
                }
            }
            else
            {
                if(roleFunctionilitiesSet.contains(functionality.getId())) {
                    removeFunctionilitiesSet.add(functionality.getId());
                }
            }
        }
        
        List<Functionality> newRoleFunctionilities = new ArrayList<>();
        for (Functionality functionality : roleFunctionilities)
        {
            if(!removeFunctionilitiesSet.contains(functionality.getId())) {
                newRoleFunctionilities.add(functionality);   
            }
        }
        
        roleFunctionilities.clear();
        roleFunctionilities.addAll(newRoleFunctionilities);

        UserAccountRole userAccountRole = this.update(userRolrId, userAccRole, operatorId, languageId);
        
        return userAccountRole != null;
    }
}
