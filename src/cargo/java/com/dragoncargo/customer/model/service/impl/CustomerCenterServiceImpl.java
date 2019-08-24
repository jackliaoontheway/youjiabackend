package com.dragoncargo.customer.model.service.impl;


import com.dragoncargo.customer.model.CustomerCenter;
import com.dragoncargo.customer.model.repository.CustomerCenterRepos;
import com.dragoncargo.customer.model.service.CustomerCenterService;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.service.ChargeRateItemService;
import com.polarj.model.FieldSpecification;
import com.polarj.model.UserAccount;
import com.polarj.model.service.impl.EntityServiceImpl;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerCenterServiceImpl extends EntityServiceImpl<CustomerCenter, Integer>
		implements CustomerCenterService {

	@Autowired
	private ChargeRateItemService chargeRateItemService;

	@Override
	public CustomerCenter create(CustomerCenter entity, Integer operId, String languageId) {
		if (entity != null && entity.getId() == null) {
			if(entity.getCustomerInfo() != null){
				entity.setCustomerCode(entity.getCustomerInfo().getShorthandCode()+"-"+entity.getCustomerInfo().getAbbreviation());
			}
		    UserAccount u = new UserAccount();
			u.setId(operId);
			entity.setSever(u);
			return super.create(entity, operId, languageId);
		}
		return null;
	}

	@Override
	public CustomerCenter update(Integer entityId, CustomerCenter entityWithUpdatedInfo, Integer operId, String languageId)
	{
		if(entityWithUpdatedInfo.getCustomerInfo() != null){
			entityWithUpdatedInfo.setCustomerCode(entityWithUpdatedInfo.getCustomerInfo().getShorthandCode()+"-"+entityWithUpdatedInfo.getCustomerInfo().getAbbreviation());
		}
		CustomerCenter res = super.update(entityId, entityWithUpdatedInfo, operId, languageId);
		return res;
	}

	@Override
	public List<FieldSpecification> getFieldMetaData(Class<?> clazz, UserAccount userAcc, String languageId)
	{
		List<FieldSpecification> fSpecs = super.getFieldMetaData(clazz,userAcc,languageId);
		if(!CollectionUtils.isEmpty(fSpecs)){
			for(FieldSpecification f: fSpecs){
				if(f.getName().equals("specialQuotations")){
					List<FieldSpecification> specifications = f.getComponentMetaDatas();
					for(FieldSpecification s: specifications){
						if(s.getName().equals("feeItems")){
							List<ChargeRateItem> chargeRateItemList = chargeRateItemService.list(languageId);
							List<String> selectiveValues = new ArrayList<String>();
							if (!CollectionUtils.isEmpty(fSpecs)) {
								for(ChargeRateItem c: chargeRateItemList){
									selectiveValues.add(c.getCode() + "," + c.getName());
								}
							}
							s.setSelectiveValues(selectiveValues);
						}
					}
				}
			}
		}
		return fSpecs;
	}

	@Override
	public List<CustomerCenter> listByOwner(UserAccount owner, String languageId)
	{
		CustomerCenterRepos repos = (CustomerCenterRepos) getRepos();
		List<CustomerCenter> list = new ArrayList<>();
		if (owner.isSuperAdmin())
		{
			list = super.list(languageId);
		}else {
			list = replaceI18nFieldValueWithResource(dataClone(repos.findByOwner(owner)), languageId);
		}
		return list;
	}
}
