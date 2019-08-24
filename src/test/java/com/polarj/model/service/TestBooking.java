package com.polarj.model.service;

import com.dragoncargo.sales.service.BookingOrderOperateFacade;
import com.dragoncargo.sales.service.model.BookingOrderMainModify;
import com.dragoncargo.sales.service.model.BookingOrderOperateType;
import com.polarj.TestBase;
import com.polarj.common.CommonConstant;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.UserAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class TestBooking extends TestBase
{

    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo, S extends EntityService<M, Integer>> S getService(Class<S> serviceClass)
    {
        S service = null;

        try
        {
            service = (S) SpringContextUtils.getBean(serviceClass);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            service = null;
        }
        return service;
    }

    @Test
    void testUserAccountService()
    {
        BookingOrderOperateFacade service = null;
        try
        {
            service = (BookingOrderOperateFacade) SpringContextUtils.getBean(BookingOrderOperateFacade.class);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            service = null;
        }
        Assertions.assertNotNull(service);
        saveSubSystenUserAccount(service);
    }

    void saveSubSystenUserAccount(BookingOrderOperateFacade service)
    {

        try
        {
            BookingOrderMainModify mainModify = new BookingOrderMainModify();
            mainModify.setHawb("test111");
            mainModify.setMawb("test222");
            mainModify.setPickupLocationCode("2342");
            mainModify.setReceivingCityCode("222312");
            mainModify.setTransferNeeded(true);
            service.modifyMainInfo(27,mainModify, BookingOrderOperateType.UPDATE, new UserAccount(), CommonConstant.defaultSystemLanguage);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

    }

}
