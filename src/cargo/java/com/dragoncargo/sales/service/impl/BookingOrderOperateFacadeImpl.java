package com.dragoncargo.sales.service.impl;

import com.dragoncargo.sales.service.BookingOrderModifyService;
import com.dragoncargo.sales.service.BookingOrderOperateFacade;
import com.dragoncargo.sales.service.model.BookingGoodsAttributeModify;
import com.dragoncargo.sales.service.model.BookingOrderMainModify;
import com.dragoncargo.sales.service.model.BookingOrderOperateType;
import com.dragoncargo.sales.service.model.BookingQuotationItemModify;
import com.polarj.model.UserAccount;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BookingOrderOperateFacadeImpl implements BookingOrderOperateFacade
{

//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "BookingOrderMainModifyService")
    private BookingOrderModifyService bookingOrderMainModifyService;

    @Resource(name = "BookingGoodsAttributeModifyService")
    private BookingOrderModifyService bookingGoodsAttributeModifyService;

    @Resource(name = "BookingQuotationItemModifyService")
    private BookingOrderModifyService bookingQuotationItemModifyService;

    @Override
    public Boolean modifyMainInfo(Integer orderId, BookingOrderMainModify data, BookingOrderOperateType type, UserAccount userAcc, String languageId) {
        return bookingOrderMainModifyService.select2Operate(orderId, data, type, userAcc,languageId);
    }

    @Override
    public Boolean modifyGoodsAttr(Integer orderId, BookingGoodsAttributeModify data, BookingOrderOperateType type, UserAccount userAcc, String languageId) {
        return bookingGoodsAttributeModifyService.select2Operate(orderId, data, type, userAcc,languageId);
    }

    @Override
    public Boolean modifyChargeItem(Integer orderId, BookingQuotationItemModify data, BookingOrderOperateType type, UserAccount userAcc, String languageId) {
        return bookingQuotationItemModifyService.select2Operate(orderId, data, type, userAcc,languageId);
    }

//    private <T extends GenericDbInfo> Boolean modifyData(Integer orderId, T data, BookingOrderOperateType type, UserAccount userAcc, String languageId){
//        try {
//            BookingOrderModifyService service = SpringContextUtils.getBeanByName(data.getClass().getSimpleName()+ "Service");
//            if(service == null){
//                logger.debug("找不到对应的Booking Order Modify Service: {}",this.getClass().getSimpleName());
//                return false;
//            }
//            return service.select2Operate(orderId,data,type,userAcc,languageId);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

}
