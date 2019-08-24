package com.dragoncargo.sales.model.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.service.ChargeRateItemService;
import com.polarj.model.FieldSpecification;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.model.service.BookingOrderService;
import com.polarj.model.UserAccount;
import com.polarj.model.service.SerialNumberService;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class BookingOrderServiceImpl extends EntityServiceImpl<BookingOrder, Integer> implements BookingOrderService
{

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private ChargeRateItemService chargeRateItemService;

    @Override
    public List<FieldSpecification> getFieldMetaData(Class<?> clazz, UserAccount userAcc, String languageId)
    {
        List<FieldSpecification> fSpecs = super.getFieldMetaData(clazz,userAcc,languageId);
        if(!CollectionUtils.isEmpty(fSpecs)) {
            for (FieldSpecification f : fSpecs) {
                if (f.getName().equals("specialQuotations")) {
                    List<FieldSpecification> specifications = f.getComponentMetaDatas();
                    for (FieldSpecification s : specifications) {
                        if (s.getName().equals("feeItems")) {
                            List<ChargeRateItem> chargeRateItemList = chargeRateItemService.list(languageId);
                            List<String> selectiveValues = new ArrayList<String>();
                            if (!CollectionUtils.isEmpty(fSpecs)) {
                                for (ChargeRateItem c : chargeRateItemList) {
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
    public BookingOrder generateBookingOrder(BookingOrder entity, UserAccount userAcc, String languageId)
    {

        if (entity != null && entity.getId() == null)
        {
            entity.setOrderNum(serialNumberService.generateSerialNumberByModelCode("bookingorder"));
            String pid = entity.getOrderNum() + "@" + UUID.randomUUID().toString();
            entity.setWfPid(pid);
            entity.setOwner(userAcc);
            return super.create(entity, userAcc.getId(), languageId);
        }
        return null;
    }

    @Override
    public BookingOrder cloneBookingOrder(BookingOrder entity, UserAccount userAcc, String languageId)
    {
        if (entity != null && entity.getId() == null)
        {
            entity.setOrderNum(serialNumberService.generateSerialNumberByModelCode("bookingorder"));
            String pid = entity.getOrderNum() + "@" + UUID.randomUUID().toString();
            entity.setWfPid(pid);
            entity.setId(null);
            return super.create(entity, userAcc.getId(), languageId);
        }
        return null;
    }

}
