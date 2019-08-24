package com.dragoncargo.omm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.service.ChargeRateItemCategoryService;
import com.dragoncargo.omm.service.ChargeRateQueryFacade;
import com.dragoncargo.omm.service.ChargeRateQueryService;
import com.dragoncargo.omm.service.model.AirportLocalChargeRateData;
import com.dragoncargo.omm.service.model.AirportLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.AirportWarehouseLocalChargeRateData;
import com.dragoncargo.omm.service.model.AirportWarehouseLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.AviationCompanyLocalChargeRateData;
import com.dragoncargo.omm.service.model.AviationCompanyLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.FreightChargeRateData;
import com.dragoncargo.omm.service.model.FreightChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.FreightSubChargeRateData;
import com.dragoncargo.omm.service.model.FreightSubChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.InHouseLocalChargeRateData;
import com.dragoncargo.omm.service.model.InHouseLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.PickupChargeRateData;
import com.dragoncargo.omm.service.model.PickupChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryChargeRateComponentResponse;
import com.dragoncargo.omm.service.model.QueryChargeRateResponse;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.TransferChargeRateData;
import com.dragoncargo.omm.service.model.TransferChargeRateQueryCriteria;

@Service
public class ChargeRateQueryFacadeImpl implements ChargeRateQueryFacade
{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "FreightRateQueryService")
    private ChargeRateQueryService<FreightChargeRateQueryCriteria, FreightChargeRateData> freightChargeRateQueryService;

    @Resource(name = "AviationCompanyLocalChargeRateQueryService")
    private ChargeRateQueryService<AviationCompanyLocalChargeRateQueryCriteria,
            AviationCompanyLocalChargeRateData> aviationCompanyLocalChargeRateQueryService;

    @Resource(name = "AirportWarehouseLocalChargeRateQueryService")
    private ChargeRateQueryService<AirportWarehouseLocalChargeRateQueryCriteria,
            AirportWarehouseLocalChargeRateData> airportWarehouseLocalChargeRateQueryService;

    @Resource(name = "AirportLocalChargeRateQueryService")
    private ChargeRateQueryService<AirportLocalChargeRateQueryCriteria,
            AirportLocalChargeRateData> airportLocalChargeRateQueryService;

    @Resource(name = "FreightSubChargeRateQueryService")
    private ChargeRateQueryService<FreightSubChargeRateQueryCriteria,
            FreightSubChargeRateData> freightSubChargeRateQueryService;

    @Resource(name = "InHouseLocalChargeRateQueryService")
    private ChargeRateQueryService<InHouseLocalChargeRateQueryCriteria,
            InHouseLocalChargeRateData> inHouseLocalChargeRateQueryService;

    @Resource(name = "PickupChargeRateQueryService")
    private ChargeRateQueryService<PickupChargeRateQueryCriteria, PickupChargeRateData> pickupChargeRateQueryService;

    @Resource(name = "TransferChargeRateQueryService")
    private ChargeRateQueryService<TransferChargeRateQueryCriteria,
            TransferChargeRateData> transferChargeRateQueryService;

    @Resource
    private ChargeRateItemCategoryService chargeRateItemCategoryService;

    @Override
    public QueryChargeRateComponentResponse multipleQuery(QueryCriteria queryCriteria)
    {
        QueryChargeRateComponentResponse responseComponent = new QueryChargeRateComponentResponse();

        QueryChargeRateResponse<FreightChargeRateData> freightChargeRateQueryResponse = freightChargeRateQueryService.query(queryCriteria, "FreightRate");
        responseComponent
                .setFreightChargeRateDataResponse(freightChargeRateQueryResponse);

        responseComponent.setAviationCompanyLocalChargeRateDataResponse(
                aviationCompanyLocalChargeRateQueryService.query(queryCriteria, "AviationCompanyLocalChargeRate"));

        responseComponent.setAirportWarehouseLocalChargeRateDataResponse(
                airportWarehouseLocalChargeRateQueryService.query(queryCriteria, "AirportWarehouseLocalChargeRate"));

        responseComponent.setAirportLocalChargeRateDataResponse(
                airportLocalChargeRateQueryService.query(queryCriteria, "AirportLocalChargeRate"));

        responseComponent.setFreightSubChargeRateDataResponse(
                freightSubChargeRateQueryService.query(queryCriteria, "FreightSubChargeRate"));

        responseComponent.setInHouseLocalChargeRateDataResponse(
                inHouseLocalChargeRateQueryService.query(queryCriteria, "InHouseLocalChargeRate"));

        responseComponent.setPickupChargeRateDataResponse(
                pickupChargeRateQueryService.query(queryCriteria, "PickupChargeRate"));
        
        if(freightChargeRateQueryResponse != null && freightChargeRateQueryResponse.getChargeRateData() != null
                && freightChargeRateQueryResponse.getChargeRateData().size() != 0) {
            
            Map<String,FreightChargeRateData> map = new HashMap<>();
            for(FreightChargeRateData data : freightChargeRateQueryResponse.getChargeRateData()) {
                if(data.isTransferNeeded()) {
                    String key  = "";
                    if(data.getFlightData() == null || data.getFlightData().getAviationCompanyCode() == null) {
                        continue;
                    }
                    key += data.getFlightData().getAviationCompanyCode();
                    if(data.getFlightData().getArrivingAirPortCode() != null) {
                        key += data.getFlightData().getArrivingAirPortCode();
                    }
                    map.put(key, data);   
                }
            }
            for(FreightChargeRateData data : map.values()) {
                queryCriteria.setArrivingAirPortCode(data.getFlightData().getArrivingAirPortCode());
                queryCriteria.setAviationCompanyCode(data.getFlightData().getAviationCompanyCode());
                responseComponent.setTransferChargeRateDataResponse(
                        transferChargeRateQueryService.query(queryCriteria, "TransferChargeRate"));
            }
            
        }

        return responseComponent;
    }

    @Override
    public QueryChargeRateComponentResponse simpleQuery(QueryCriteria queryCriteria)
    {
        QueryChargeRateComponentResponse responseComponent = new QueryChargeRateComponentResponse();

        responseComponent
                .setFreightChargeRateDataResponse(freightChargeRateQueryService.query(queryCriteria, "FreightRate"));

        return responseComponent;
    }

}
