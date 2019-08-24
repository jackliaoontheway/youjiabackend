package com.dragoncargo.sales.model.service.impl;

import com.dragoncargo.customer.model.CustomerCenter;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.service.ChargeRateItemService;
import com.dragoncargo.sales.model.*;
import com.dragoncargo.sales.model.service.BookingOrderService;
import com.polarj.common.CommonConstant;
import com.polarj.model.FieldSpecification;
import com.polarj.model.UserAccount;
import com.polarj.model.service.SerialNumberService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuoteSchemeServiceImpl extends QuoteSchemeServiceHelper{

	@Autowired
	private SerialNumberService serialNumberService;

	@Autowired
	private BookingOrderService bookingOrderService;

	@Autowired
	private ChargeRateItemService chargeRateItemService;

	@Override
	public BookingOrder generateBookingOrder(QuoteScheme quoteScheme, UserAccount userAcc) {

		if(quoteScheme == null){
			return null;
		}
		try {
			BookingOrder bookingOrder = tran2BookingOrder(quoteScheme);
			if(bookingOrder == null){
				return null;
			}
			BookingOrder response = bookingOrderService.generateBookingOrder(bookingOrder,userAcc, CommonConstant.defaultSystemLanguage);
			if(response != null){
				update(quoteScheme.getId(),quoteScheme,userAcc.getId(), CommonConstant.defaultSystemLanguage);
			}
			return response;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<QuoteScheme> generateScheme(QueryCondition queryCondition) {
		List<QuoteScheme> schemeList = new ArrayList<>();
		if(queryCondition != null){
			schemeList = requestAllFeeItem(queryCondition);
		}
		return schemeList;
	}

	@Override
	public QuoteScheme create(QuoteScheme entity, Integer operId, String languageId) {
		if (entity != null && entity.getId() == null) {
			entity.setSchemeNum(serialNumberService.generateSerialNumberByModelCode("quoteScheme"));
			return super.create(entity, operId, languageId);
		}
		return null;
	}

	@Override
	public Page<QuoteScheme> listByCriteria(QuoteScheme entity, String sortField, boolean desc, String languageId,
											  Integer dataLevel) {
		if (entity == null) {
			return null;
		}
		// 这里列表显示取出4层节点
		Page<QuoteScheme> res = super.listByCriteria(entity, sortField, desc,
				languageId, 4);
		return res;
	}


	@Override
	public List<FieldSpecification> getFieldMetaData(Class<?> clazz, UserAccount userAcc, String languageId)
	{
		List<FieldSpecification> fSpecs = super.getFieldMetaData(clazz,userAcc,languageId);
		if(!CollectionUtils.isEmpty(fSpecs)){
			for(FieldSpecification f: fSpecs){
				if(f.getName().equals("customerCenter")){
					List<FieldSpecification>customerfSpecs = super.getFieldMetaData(CustomerCenter.class,userAcc,languageId);
					f.setComponentMetaDatas(customerfSpecs);
				}
				if(f.getName().equals("quoteSpecialFees")){
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
}
