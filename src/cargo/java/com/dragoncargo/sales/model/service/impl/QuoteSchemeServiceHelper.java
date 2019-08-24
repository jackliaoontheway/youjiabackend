package com.dragoncargo.sales.model.service.impl;


import com.dragoncargo.customer.model.CustomerCenter;
import com.dragoncargo.customer.model.SpecialQuotation;
import com.dragoncargo.customer.model.repository.CustomerCenterRepos;
import com.dragoncargo.general.model.*;
import com.dragoncargo.general.model.repository.*;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.ChargeRateType;
import com.dragoncargo.omm.model.PickupLocation;
import com.dragoncargo.omm.model.WeightSpec;
import com.dragoncargo.omm.model.repository.ChargeRateItemRepos;
import com.dragoncargo.omm.model.repository.ChargeRateTypeRepos;
import com.dragoncargo.omm.model.repository.PickupLocationRepos;
import com.dragoncargo.omm.model.repository.WeightSpecRepos;
import com.dragoncargo.omm.service.ChargeRateQueryFacade;
import com.dragoncargo.omm.service.model.*;
import com.dragoncargo.sales.model.*;
import com.dragoncargo.sales.model.service.QuoteSchemeService;
import com.polarj.model.service.impl.EntityServiceImpl;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public abstract class QuoteSchemeServiceHelper extends EntityServiceImpl<QuoteScheme, Integer>
		implements QuoteSchemeService {

	@Autowired
	private ChargeRateQueryFacade chargeRateQueryFacade;

	@Autowired
	private AirlineRepos airlineRepos;

	@Autowired
	private AviationCompanyRepos aviationCompanyRepos;

	@Autowired
	private ChargeUnitRepos chargeUnitRepos;

	@Autowired
	private WeightTypeRepos weightTypeRepos;

	@Autowired
	private AirPortInfoRepos airPortInfoRepos;

	@Autowired
	private CityCodeRepos cityCodeRepos;

	@Autowired
	private ChargeRateTypeRepos chargeRateTypeRepos;

	@Autowired
	private AirportWarehouseRepos airportWarehouseRepos;

	@Autowired
	private WeightSpecRepos weightSpecRepos;

	@Autowired
	private PickupLocationRepos pickupLocationRepos;

	@Autowired
	private CustomerCenterRepos customerCenterRepos;

	@Autowired
	private ChargeRateItemRepos chargeRateItemRepos;

	/**
	 * 选中方案转为订单
	 * @param quoteScheme
	 * @return
	 */
	protected BookingOrder tran2BookingOrder(QuoteScheme quoteScheme){
		BookingOrder bookingOrder = new BookingOrder();
		List<QuoteChargeItem> quoteChargeItem = quoteScheme.getQuoteChargeItem();
		QuoteChargeItem freightItem = getCheckFreight(quoteChargeItem);
		if(freightItem == null){
			return null;
		}

		WeightSpec weightSpec= weightSpecRepos.findByCode(freightItem.getQuoteFreightFee().getCode());
		if(weightSpec == null){
			return null;
		}
		List<QuoteChargeItem> otherItems = quoteChargeItem.stream().filter(e->(!e.getChargeRateItem().getChargeRateCategory().getCode().equals(ChargeTypeCode.FreightRate.name()))).collect(Collectors.toList());
		List<BookingQuotationItem> bookingQuotationItemList = tranChargeItemToBookingOrder(freightItem,otherItems);
		List<BookingQuotationItem> bookingActualQuotationItemList = new ArrayList<>();
		for(BookingQuotationItem item:bookingQuotationItemList){
			BookingQuotationItem newItem = new BookingQuotationItem();
			BeanUtils.copyProperties(item,newItem);
			bookingActualQuotationItemList.add(newItem);
		}
		bookingOrder.setForecastQuotations(bookingQuotationItemList);

		bookingOrder.setActualQuotations(bookingActualQuotationItemList);
		BookingGoodsAttribute attribute = new BookingGoodsAttribute();
		QueryCondition queryCondition = quoteScheme.getQueryCondition();
		attribute.setGoodsName(queryCondition.getGoodsName());
		attribute.setQuantity(queryCondition.getQuantity());
		attribute.setVolume(queryCondition.getVolume());
		attribute.setWeight(queryCondition.getWeight());
		attribute.setWeightSpec(weightSpec);
		attribute.setBulkyCargoDiscountRate(quoteScheme.getBubbleRate());
		bookingOrder.setForecastGoogsAttribute(attribute);
		bookingOrder.setCustomerCenter(quoteScheme.getCustomerCenter());
		bookingOrder.setTransferNeeded(freightItem.getQuoteFreightFee().getTransferNeeded());
		bookingOrder.setAirportWarehouse(quoteScheme.getAirportWarehouse());
		List<QuoteSpecialFee> quoteSpecialFeeList = quoteScheme.getQuoteSpecialFees();
		List<BookingSpecialQuotation> bookingSpecialFeeList = new ArrayList<>();
		if(quoteSpecialFeeList != null && quoteSpecialFeeList.size()>0){
			for(QuoteSpecialFee s: quoteSpecialFeeList){
				BookingSpecialQuotation specialFee = new BookingSpecialQuotation();
				BeanUtils.copyProperties(s,specialFee);
				specialFee.setId(null);
				bookingSpecialFeeList.add(specialFee);
			}
		}
		bookingOrder.setSpecialQuotations(bookingSpecialFeeList);
		return bookingOrder;
	}

	/**
	 * 	询价生成方案
	 */
	protected List<QuoteScheme> requestAllFeeItem(QueryCondition queryCondition){
		List<FreightChargeRateData> freightChargeRateDataList = new ArrayList<>();
		List<AirportLocalChargeRateData> airportLocalChargeRateDataList = new ArrayList<>();
		List<AirportWarehouseLocalChargeRateData> airportWarehouseLocalChargeRateDataList = new ArrayList<>();
		List<AviationCompanyLocalChargeRateData> aviationCompanyLocalChargeRateDataList = new ArrayList<>();
		List<TransferChargeRateData> transferChargeRateDataList = new ArrayList<>();
		List<PickupChargeRateData> pickupChargeRateDataList = new ArrayList<>();
		List<FreightSubChargeRateData> freightSubChargeRateDataList = new ArrayList<>();
		List<InHouseLocalChargeRateData> InHouseLocalChargeRateDataList = new ArrayList<>();

		List<QuoteScheme> schemeList = new ArrayList<>();

		try {
			QueryCriteria queryCriteria  = generateQueryCriteria(queryCondition);
			QueryChargeRateComponentResponse response = new QueryChargeRateComponentResponse();
			if((queryCondition.getWeight().getValue() == null && queryCondition.getWeight().getUnit() == null) && (queryCondition.getVolume().getValue() == null && queryCondition.getVolume().getUnit() == null )){
				response = chargeRateQueryFacade.simpleQuery(queryCriteria);
			}else {
				response = chargeRateQueryFacade.multipleQuery(queryCriteria);
			}
			if(response.getFreightChargeRateDataResponse()!= null && response.getFreightChargeRateDataResponse().getChargeRateData() != null){
				freightChargeRateDataList = response.getFreightChargeRateDataResponse().getChargeRateData();
			}
			if(response.getAviationCompanyLocalChargeRateDataResponse() != null && response.getAviationCompanyLocalChargeRateDataResponse().getChargeRateData() != null){
				aviationCompanyLocalChargeRateDataList = response.getAviationCompanyLocalChargeRateDataResponse().getChargeRateData();
			}

			if(response.getAirportLocalChargeRateDataResponse() != null && response.getAirportLocalChargeRateDataResponse().getChargeRateData() != null){
				airportLocalChargeRateDataList = response.getAirportLocalChargeRateDataResponse().getChargeRateData();
			}
			if(response.getAirportWarehouseLocalChargeRateDataResponse() != null && response.getAirportWarehouseLocalChargeRateDataResponse().getChargeRateData() != null){
				airportWarehouseLocalChargeRateDataList = response.getAirportWarehouseLocalChargeRateDataResponse().getChargeRateData();
			}
			if(response.getTransferChargeRateDataResponse() != null && response.getTransferChargeRateDataResponse().getChargeRateData() != null){
				transferChargeRateDataList = response.getTransferChargeRateDataResponse().getChargeRateData();
			}
			if(response.getPickupChargeRateDataResponse() != null && response.getPickupChargeRateDataResponse().getChargeRateData() != null){
				pickupChargeRateDataList = response.getPickupChargeRateDataResponse().getChargeRateData();
			}
			if(response.getFreightSubChargeRateDataResponse() != null && response.getFreightSubChargeRateDataResponse().getChargeRateData() != null){
				freightSubChargeRateDataList = response.getFreightSubChargeRateDataResponse().getChargeRateData();
			}
			if(response.getInHouseLocalChargeRateDataResponse() != null && response.getInHouseLocalChargeRateDataResponse().getChargeRateData() != null){
				InHouseLocalChargeRateDataList = response.getInHouseLocalChargeRateDataResponse().getChargeRateData();
			}

			Map<String,ChargeUnit> unitMap =  getChargeUnitMap();
			Map<String,WeightType> weightTypeMap = getWeightTypeMap();
			Map<String, ChargeRateItem> chargeRateItemMap = getChargeRateItemMap();

			// 本地航司费分组
			Map<String,List<AviationCompanyLocalChargeRateData>> companyLocalMap = aviationCompanyLocalChargeRateDataList.stream().collect(Collectors.groupingBy(e->(
					e.getAviationCompanyCode()
			)));

			// 转运费分组
			Map<String,List<TransferChargeRateData>> transferMap = transferChargeRateDataList.stream().collect(Collectors.groupingBy(e->(
					e.getArrivingAirPortCode()
			)));

			// 运费通过航司分组
			Map<String,List<FreightChargeRateData>> companyMap = freightChargeRateDataList.stream().collect(Collectors.groupingBy(e->(
					e.getFlightData().getAviationCompanyCode()
			)));

			// 将询价返回的机场本地费用进行转换为费用项
			List<QuoteChargeItem>  airportLocalFeeList = generateQuoteChargeItem(airportLocalChargeRateDataList,unitMap,weightTypeMap,chargeRateItemMap);

			// 将询价返回的机场货站费用进行转换为费用项
			List<QuoteChargeItem>  airportWarehouseLocalFeeList = generateQuoteChargeItem(airportWarehouseLocalChargeRateDataList,unitMap,weightTypeMap,chargeRateItemMap);

			// 将询价返回的提货费用进行转换为费用项
			List<QuoteChargeItem>  pickupChargeFeeList = generateQuoteChargeItem(pickupChargeRateDataList,unitMap,weightTypeMap,chargeRateItemMap);

			// 将询价返回的运费附加费进行转换为费用项
			List<QuoteChargeItem>  freightSubChargeFeeList = generateQuoteChargeItem(freightSubChargeRateDataList,unitMap,weightTypeMap,chargeRateItemMap);

			// 将询价返回的公司本地费用进行转换为费用项
			List<QuoteChargeItem>  InHouseLocalChargeFeeList = generateQuoteChargeItem(InHouseLocalChargeRateDataList,unitMap,weightTypeMap,chargeRateItemMap);

			for(Map.Entry<String,List<FreightChargeRateData>> companyEntry:companyMap.entrySet()) {
				List<FreightChargeRateData> companyFees = companyEntry.getValue();
				String companyCode = companyEntry.getKey();
				AviationCompany aviationCompany = aviationCompanyRepos.findByCode(companyCode);

				//运费通过航司分组后,再次通过航线进行分组
				Map<String, List<FreightChargeRateData>> airlineMap = companyFees.stream().collect(Collectors.groupingBy(e -> (
						e.getFlightData().getAirlineCode()
				)));

				// 取出航司本队费用
				List<AviationCompanyLocalChargeRateData> companyLocalChargeList = companyLocalMap.get(companyCode);
				List<QuoteChargeItem>  companyLocalFeeList = generateQuoteChargeItem(companyLocalChargeList,unitMap,weightTypeMap,chargeRateItemMap);

				// 对运费每条数据进行处理整合
				for(Map.Entry<String,List<FreightChargeRateData>> airlineEntry:airlineMap.entrySet()){
					List<FreightChargeRateData> airlineFees= airlineEntry.getValue();
					String airlineCode = airlineEntry.getKey();
					Airline airline = airlineRepos.findByCode(airlineCode);
					List<TransferChargeRateData> transferChargeRateDatas = transferMap.get(airline.getArrivingAirPort().getCode());
					List<QuoteChargeItem> transferChargeFeeList = generateQuoteChargeItem(transferChargeRateDatas,unitMap,weightTypeMap,chargeRateItemMap);

					QuoteScheme scheme = new QuoteScheme();
					scheme.setAviationCompany(aviationCompany);
					scheme.setAirline(airline);
					if(!CollectionUtils.isEmpty(airportWarehouseLocalChargeRateDataList)){
						AirportWarehouse airportWarehouse = airportWarehouseRepos.findByCode(airportWarehouseLocalChargeRateDataList.get(0).getAirportWarehouseCode());
						if(airportWarehouse != null){
							scheme.setAirportWarehouse(airportWarehouse);
						}
					}
					// 预留航线设置
//					scheme.setFlightList(new ArrayList<>());
					scheme.setMemo("");
					scheme.setPrescription("");
					if(queryCondition.getCustomerCenter() != null && queryCondition.getCustomerCenter().getId() != null){
						CustomerCenter customerCenter = dataClone(customerCenterRepos.getOne(queryCondition.getCustomerCenter().getId()));
						scheme.setCustomerCenter(customerCenter);
						List<SpecialQuotation> specialQuotationList = customerCenter.getSpecialQuotations();
						List<QuoteSpecialFee> specialFeeList = new ArrayList<>();
						if(specialQuotationList != null && specialQuotationList.size()>0){
							for(SpecialQuotation s: specialQuotationList){
								QuoteSpecialFee specialFee = new QuoteSpecialFee();
								BeanUtils.copyProperties(s,specialFee);
								specialFee.setId(null);
								specialFeeList.add(specialFee);
							}
						}
						scheme.setQuoteSpecialFees(specialFeeList);
					}
					scheme.setQueryCondition(queryCondition);
					List<QuoteChargeItem>  itemList = generateFreightChargeItem(airlineFees,unitMap,weightTypeMap,chargeRateItemMap);

					if(!CollectionUtils.isEmpty(companyLocalFeeList)) {
						itemList.addAll(companyLocalFeeList);
					}
					if(!CollectionUtils.isEmpty(airportLocalFeeList)) {
						itemList.addAll(airportLocalFeeList);
					}
					if(!CollectionUtils.isEmpty(airportWarehouseLocalFeeList)) {
						itemList.addAll(airportWarehouseLocalFeeList);
					}
					if(!CollectionUtils.isEmpty(pickupChargeFeeList)) {
						itemList.addAll(pickupChargeFeeList);
					}
					if(!CollectionUtils.isEmpty(freightSubChargeFeeList)) {
						itemList.addAll(freightSubChargeFeeList);
					}
					if(!CollectionUtils.isEmpty(InHouseLocalChargeFeeList)) {
						itemList.addAll(InHouseLocalChargeFeeList);
					}
					if(!CollectionUtils.isEmpty(transferChargeFeeList)) {
						itemList.addAll(transferChargeFeeList);
					}
					scheme.setQuoteChargeItem(itemList);
					schemeList.add(scheme);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return schemeList;
	}

	private QuoteChargeItem getCheckFreight(List<QuoteChargeItem> quoteChargeItem){
		if(CollectionUtils.isEmpty(quoteChargeItem)){
			return null;
		}
		for(QuoteChargeItem item: quoteChargeItem){
			if(item.getChargeRateItem().getChargeRateCategory().getCode().equals(ChargeTypeCode.FreightRate.name())){
				if(item.getQuoteFreightFee() != null && item.getQuoteFreightFee().getIsCheck()){
					return item;
				}
			}
		}
		return null;
	}

	private List<BookingQuotationItem> tranChargeItemToBookingOrder(QuoteChargeItem freightItem,List<QuoteChargeItem> quoteChargeItems){
		List<BookingQuotationItem> itemList =  new ArrayList<>();
		itemList.addAll(tranByChargeItem(freightItem));
		if(!CollectionUtils.isEmpty(quoteChargeItems)){
			for(QuoteChargeItem quoteChargeItem : quoteChargeItems) {
				itemList.addAll(tranByChargeItem(quoteChargeItem));
			}
		}
		return itemList;
	}


	private List<BookingQuotationItem> tranByChargeItem(QuoteChargeItem quoteChargeItem){
		List<BookingQuotationItem> items =  new ArrayList<>();

		if(quoteChargeItem.getUnitPriceInfo() != null){
			BookingQuotationItem item = new BookingQuotationItem();
			item.setChargeName(quoteChargeItem.getChargeRateItem().getCode());
			item.setChargeRateType("RATE_FOR_PUBLIC");
			item.setChargeUnit(quoteChargeItem.getChargeUnit().getCode());
			item.setMaximumCharge(quoteChargeItem.getMaximumCharge());
			item.setMinimumCharge(quoteChargeItem.getMinimumCharge());
			item.setUnitPrice(quoteChargeItem.getUnitPriceInfo().getUnitPrice());
			if(quoteChargeItem.getUnitPriceInfo() != null && quoteChargeItem.getUnitPriceInfo().getWeightType() != null){
				item.setWeightType(quoteChargeItem.getUnitPriceInfo().getWeightType().getCode());
			}
			items.add(item);
		}
		if(quoteChargeItem.getMinSalesPriceInfo() != null){
			BookingQuotationItem item = new BookingQuotationItem();
			item.setChargeName(quoteChargeItem.getChargeRateItem().getCode());
			item.setChargeRateType("RATE_FOR_MINIMAL");
			item.setChargeUnit(quoteChargeItem.getChargeUnit().getCode());
			item.setMaximumCharge(quoteChargeItem.getMaximumCharge());
			item.setMinimumCharge(quoteChargeItem.getMinimumCharge());
			item.setUnitPrice(quoteChargeItem.getMinSalesPriceInfo().getUnitPrice());
			if(quoteChargeItem.getMinSalesPriceInfo() != null && quoteChargeItem.getMinSalesPriceInfo().getWeightType() != null){
				item.setWeightType(quoteChargeItem.getMinSalesPriceInfo().getWeightType().getCode());
			}
			items.add(item);
		}

		if(quoteChargeItem.getSalesCostPriceInfo() != null){
			BookingQuotationItem item = new BookingQuotationItem();
			item.setChargeName(quoteChargeItem.getChargeRateItem().getCode());
			item.setChargeRateType("RATE_FOR_SALES");
			item.setChargeUnit(quoteChargeItem.getChargeUnit().getCode());
			item.setMaximumCharge(quoteChargeItem.getMaximumCharge());
			item.setMinimumCharge(quoteChargeItem.getMinimumCharge());
			item.setUnitPrice(quoteChargeItem.getSalesCostPriceInfo().getUnitPrice());
			if(quoteChargeItem.getSalesCostPriceInfo() != null && quoteChargeItem.getSalesCostPriceInfo().getWeightType() != null){
				item.setWeightType(quoteChargeItem.getSalesCostPriceInfo().getWeightType().getCode());
			}
			items.add(item);
		}

		if(quoteChargeItem.getCompanyCostPriceInfo() != null){
			BookingQuotationItem item = new BookingQuotationItem();
			item.setChargeName(quoteChargeItem.getChargeRateItem().getCode());
			item.setChargeRateType("RATE_FOR_COMPANY");
			item.setChargeUnit(quoteChargeItem.getChargeUnit().getCode());
			item.setMaximumCharge(quoteChargeItem.getMaximumCharge());
			item.setMinimumCharge(quoteChargeItem.getMinimumCharge());
			if(quoteChargeItem.getCompanyCostPriceInfo() != null && quoteChargeItem.getCompanyCostPriceInfo().getWeightType() != null){
				item.setWeightType(quoteChargeItem.getCompanyCostPriceInfo().getWeightType().getCode());
			}
			item.setUnitPrice(quoteChargeItem.getCompanyCostPriceInfo().getUnitPrice());
			items.add(item);
		}
		return items;
	}


	private QueryCriteria generateQueryCriteria(QueryCondition queryCondition){
		QueryCriteria queryCriteria = new QueryCriteria();
		VolumnData volumnData = new VolumnData();
		volumnData.setUnit(DimensionsUnit.M);
		volumnData.setValue(queryCondition.getVolume().getValue());
		queryCriteria.setVolumnData(volumnData);
		if(queryCondition.getDepartAirPort() != null){
			AirPortInfo airPortInfo = dataClone(airPortInfoRepos.getOne(queryCondition.getDepartAirPort().getId()));
			queryCondition.setDepartAirPort(airPortInfo);
			queryCriteria.setDepartAirPortCode(airPortInfo.getCode());
		}
		if(queryCondition.getToCity() != null){
			CityCode cityCode = dataClone(cityCodeRepos.getOne(queryCondition.getToCity().getId()));
			queryCondition.setToCity(cityCode);
			queryCriteria.setToCityCode(cityCode.getCode());
		}
//		queryCriteria.setDepartAirPortCode("SZSS");
//		queryCriteria.setToCityCode("NY");
		queryCriteria.setQueryAdjacentRange(true);
		List<String> stringList = new ArrayList<>();
		List<ChargeRateType> chargeRateTypeList = chargeRateTypeRepos.findAll();
		for(ChargeRateType c : chargeRateTypeList){
			stringList.add(c.getCode());
		}
		queryCriteria.setChargeRateTypeList(stringList);
		WeightData weightData = new WeightData();
		weightData.setUnit(queryCondition.getWeight().getUnit());
		weightData.setValue(queryCondition.getWeight().getValue());
		queryCriteria.setWeight(weightData);

		if(queryCondition.getPickupLocation() != null){
			PickupLocation pickupLocation = dataClone(pickupLocationRepos.getOne(queryCondition.getPickupLocation().getId()));
			queryCondition.setPickupLocation(pickupLocation);
			queryCriteria.setPickupLocation(pickupLocation.getCode());
		}
		return queryCriteria;
	}

	private Map<String,ChargeUnit> getChargeUnitMap(){
		List<ChargeUnit> chargeUnitList = chargeUnitRepos.findAll();
		Map<String,ChargeUnit> unitMap = new HashMap<>();
		chargeUnitList.stream().forEach(e->{
			if(unitMap.get(e.getCode()) == null){
				unitMap.put(e.getCode(),e);
			}
		});
		return unitMap;
	}

	private Map<String,WeightType> getWeightTypeMap(){
		List<WeightType> weightTypeList = weightTypeRepos.findAll();
		Map<String,WeightType> weightTypeMap = new HashMap<>();
		weightTypeList.stream().forEach(e->{
			if(weightTypeMap.get(e.getCode()) == null){
				weightTypeMap.put(e.getCode(),e);
			}
		});
		return weightTypeMap;
	}

	private Map<String,ChargeRateItem> getChargeRateItemMap(){
		List<ChargeRateItem> chargeRateItemList = chargeRateItemRepos.findAll();
		Map<String,ChargeRateItem> chargeRateItemMap = new HashMap<>();
		chargeRateItemList.stream().forEach(e->{
			if(chargeRateItemMap.get(e.getCode()) == null){
				chargeRateItemMap.put(e.getCode(),e);
			}
		});
		return chargeRateItemMap;
	}


	private <T extends BaseChargeRateData> QuotePriceInfo tranFeeDataToFeeInfo(T info,Map<String,WeightType> weightTypeMap){
		if(info == null){
			return null;
		}
		FeeInfo feeInfo = new FeeInfo();
		feeInfo.setCurrency(info.getUnitPrice().getCurrency());
		feeInfo.setAmount(info.getUnitPrice().getAmount());

		QuotePriceInfo quotePriceInfo = new QuotePriceInfo();
		quotePriceInfo.setUnitPrice(feeInfo);
		quotePriceInfo.setWeightType(weightTypeMap.get(info.getWeightTypeCode()));
		return quotePriceInfo;
	}


	private FeeInfo tranFeeDataToFeeInfo(FeeData feeData){
		if(feeData == null){
			return null;
		}
		FeeInfo feeInfo = new FeeInfo();
		feeInfo.setCurrency(feeData.getCurrency());
		feeInfo.setAmount(feeData.getAmount());
		return feeInfo;
	}


	private Weight tranWeightData(WeightData weightData){
		if(weightData == null){
			return null;
		}
		Weight weight = new Weight();
		weight.setUnit(weightData.getUnit());
		weight.setValue(weightData.getValue());
		return weight;
	}

	private List<QuoteChargeItem>  generateFreightChargeItem(List<FreightChargeRateData> chargeRateDataList,Map<String,ChargeUnit> unitMap,Map<String,WeightType> weightTypeMap,Map<String, ChargeRateItem> chargeRateItemMap){

		Map<String, List<FreightChargeRateData>> chargeRateTypeMap = chargeRateDataList.stream().collect(Collectors.groupingBy(e->(
				e.getChargeRateTypeCode()
		)));

		List<FreightChargeRateData> publicDataList = chargeRateTypeMap.get("RATE_FOR_PUBLIC");
		List<FreightChargeRateData> miniMalDataList = chargeRateTypeMap.get("RATE_FOR_MINIMAL");
		List<FreightChargeRateData> salesDataList = chargeRateTypeMap.get("RATE_FOR_SALES");
		List<FreightChargeRateData> companyDataList = chargeRateTypeMap.get("RATE_FOR_COMPANY");

		Map<String,FreightChargeRateData> salesFeeDateMap = new HashMap<>();
		Map<String,FreightChargeRateData> miniMalFeeDateMap = new HashMap<>();
		Map<String,FreightChargeRateData> companyFeeDateMap = new HashMap<>();

		if(miniMalDataList != null && miniMalDataList.size()>0){
			miniMalDataList.stream().forEach(e->{
				if(miniMalFeeDateMap.get(e.getWeightSpecData().getCode()) == null){
					miniMalFeeDateMap.put(e.getWeightSpecData().getCode(),e);
				}
			});
		}

		if(salesDataList != null && salesDataList.size()>0){
			salesDataList.stream().forEach(e->{
				if(salesFeeDateMap.get(e.getWeightSpecData().getCode()) == null){
					salesFeeDateMap.put(e.getWeightSpecData().getCode(),e);
				}
			});
		}

		if(companyDataList != null && companyDataList.size()>0){
			companyDataList.stream().forEach(e->{
				if(companyFeeDateMap.get(e.getWeightSpecData().getCode()) == null){
					companyFeeDateMap.put(e.getWeightSpecData().getCode(),e);
				}
			});
		}

		List<QuoteChargeItem>  freightList = new ArrayList<>();
		if(publicDataList != null && publicDataList.size()>0) {
			for (FreightChargeRateData data : publicDataList) {
				QuoteChargeItem item = new QuoteChargeItem();
				item.setChargeRateItem(dataClone(chargeRateItemMap.get(data.getChargeRateCode())));
				item.setChargeUnit(unitMap.get(data.getChargeUnitCode()));
				item.setEffetiveDate(data.getEffetiveDate());
				item.setExpiredDate(data.getExpiredDate());
				item.setMaximumCharge(tranFeeDataToFeeInfo(data.getMaximumCharge()));
				item.setMinimumCharge(tranFeeDataToFeeInfo(data.getMinimumCharge()));
				item.setMemo(data.getRemark());
				item.setUnitPriceInfo(tranFeeDataToFeeInfo(data,weightTypeMap));
				item.setMinSalesPriceInfo(tranFeeDataToFeeInfo(miniMalFeeDateMap.get(data.getWeightSpecData().getCode()),weightTypeMap));
				item.setSalesCostPriceInfo(tranFeeDataToFeeInfo(salesFeeDateMap.get(data.getChargeRateCode()),weightTypeMap));
				item.setCompanyCostPriceInfo(tranFeeDataToFeeInfo(companyFeeDateMap.get(data.getWeightSpecData().getCode()),weightTypeMap));

				QuoteFreightFee freightFee = new QuoteFreightFee();
				freightFee.setCode(data.getWeightSpecData().getCode());
				freightFee.setName(data.getWeightSpecData().getName());
				freightFee.setMaxWeight(tranWeightData(data.getWeightSpecData().getMaximumRangeWeight()));
				freightFee.setMinWeight(tranWeightData(data.getWeightSpecData().getMinimumRangeWeight()));
				freightFee.setDirectFlight(data.getFlightData().getDirectFlight());
				freightFee.setTransferNeeded(data.isTransferNeeded());
				item.setQuoteFreightFee(freightFee);
				item.setEstimateAvailable(true);
				freightList.add(item);
			}
		}
		return freightList;
	}

	private <T extends BaseChargeRateData> List<QuoteChargeItem>  generateQuoteChargeItem(List<T> chargeRateDataList,Map<String,ChargeUnit> unitMap,Map<String,WeightType> weightTypeMap,Map<String, ChargeRateItem> chargeRateItemMap){
		List<QuoteChargeItem> chargeItemList = new ArrayList<>();
		if(chargeRateDataList != null && chargeRateDataList.size()>0) {
			Map<String, List<T>> chargeRateTypeMap = chargeRateDataList.stream().collect(Collectors.groupingBy(e -> (
					e.getChargeRateTypeCode()
			)));
			List<T> publicDataList = chargeRateTypeMap.get("RATE_FOR_PUBLIC");
			List<T> miniMalDataList = chargeRateTypeMap.get("RATE_FOR_MINIMAL");
			List<T> salesDataList = chargeRateTypeMap.get("RATE_FOR_SALES");
			List<T> companyDataList = chargeRateTypeMap.get("RATE_FOR_COMPANY");

			Map<String, T> miniMalFeeDateMap = new HashMap<>();
			Map<String, T> salesFeeDateMap = new HashMap<>();
			Map<String, T> companyFeeDateMap = new HashMap<>();

			if (miniMalDataList != null && miniMalDataList.size() > 0) {
				miniMalDataList.stream().forEach(e -> {
					if (miniMalFeeDateMap.get(e.getChargeRateCode()) == null) {
						miniMalFeeDateMap.put(e.getChargeRateCode(), e);
					}
				});
			}

			if (salesDataList != null && salesDataList.size() > 0) {
				salesDataList.stream().forEach(e -> {
					if (salesFeeDateMap.get(e.getChargeRateCode()) == null) {
						salesFeeDateMap.put(e.getChargeRateCode(), e);
					}
				});
			}

			if (companyDataList != null && companyDataList.size() > 0) {
				companyDataList.stream().forEach(e -> {
					if (companyFeeDateMap.get(e.getChargeRateCode()) == null) {
						companyFeeDateMap.put(e.getChargeRateCode(), e);
					}
				});
			}

			if (publicDataList != null && publicDataList.size() > 0) {
				for (T data : publicDataList) {
					QuoteChargeItem item = new QuoteChargeItem();
					item.setChargeRateItem(dataClone(chargeRateItemMap.get(data.getChargeRateCode())));
					item.setChargeUnit(unitMap.get(data.getChargeUnitCode()));
					item.setEffetiveDate(data.getEffetiveDate());
					item.setExpiredDate(data.getExpiredDate());
					item.setMaximumCharge(tranFeeDataToFeeInfo(data.getMaximumCharge()));
					item.setMinimumCharge(tranFeeDataToFeeInfo(data.getMinimumCharge()));
					item.setMemo(data.getRemark());
					item.setUnitPriceInfo(tranFeeDataToFeeInfo(data,weightTypeMap));
					item.setMinSalesPriceInfo(tranFeeDataToFeeInfo(miniMalFeeDateMap.get(data.getChargeRateCode()),weightTypeMap));
					item.setSalesCostPriceInfo(tranFeeDataToFeeInfo(salesFeeDateMap.get(data.getChargeRateCode()),weightTypeMap));
					item.setCompanyCostPriceInfo(tranFeeDataToFeeInfo(companyFeeDateMap.get(data.getChargeRateCode()),weightTypeMap));
					item.setEstimateAvailable(true);
					chargeItemList.add(item);
				}
			}
		}
		return chargeItemList;
	}
}
