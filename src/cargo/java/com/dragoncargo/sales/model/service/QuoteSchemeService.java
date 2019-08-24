package com.dragoncargo.sales.model.service;


import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.model.QueryCondition;
import com.dragoncargo.sales.model.QuoteScheme;
import com.polarj.model.UserAccount;
import com.polarj.model.service.EntityService;

import java.util.List;

public interface QuoteSchemeService extends EntityService<QuoteScheme, Integer> {

    /**
     * 选中方案生成订仓单
     * @param quoteScheme
     * @param userAcc
     * @return
     */
    public BookingOrder generateBookingOrder(QuoteScheme quoteScheme, UserAccount userAcc);

    /**
     * 查询条件返回方案
     * @param queryCondition
     * @return
     */
    public List<QuoteScheme> generateScheme(QueryCondition queryCondition);

}
