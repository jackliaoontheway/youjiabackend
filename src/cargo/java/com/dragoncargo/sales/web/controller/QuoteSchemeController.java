package com.dragoncargo.sales.web.controller;

import com.dragoncargo.sales.model.BookingOrder;
import com.dragoncargo.sales.model.QueryCondition;
import com.dragoncargo.sales.model.QuoteScheme;
import com.dragoncargo.sales.model.service.QuoteSchemeService;
import com.polarj.common.CommonConstant;
import com.polarj.common.web.controller.UserRestrictionModelController;
import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.model.UserAccount;
import com.polarj.model.enumeration.ModelOperation;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quoteSchemes")
public class QuoteSchemeController
		extends UserRestrictionModelController<QuoteScheme, Integer, QuoteSchemeService> {

	public QuoteSchemeController() {
		super(QuoteScheme.class);
	}

	@PostMapping("generateScheme")
	public @ResponseBody
	ResponseEntity<ServerResponse<QuoteScheme>> generateScheme(@RequestBody ClientRequest<QueryCondition> clientRequest){
		UserAccount userAcc = getLoginUserAccount();
		if (userAcc == null)
		{
			return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
		}
		if (!isPermitted(ModelOperation.READ.name()))
		{
			return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
		}
		List<QuoteScheme> schemeList = new ArrayList<>();
		try {
            schemeList = service.generateScheme(clientRequest.getData());
			if(!CollectionUtils.isEmpty(schemeList)){
				return generateRESTFulResponse(schemeList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return generateErrorRESTFulResponse(e.getMessage());

		}
		return generateRESTFulResponse(schemeList);
	}

	@PostMapping("generateBookingOrder")
	public @ResponseBody ResponseEntity<ServerResponse<BookingOrder>> generateBookingOrder(@RequestBody ClientRequest<QuoteScheme> clientRequest){
		UserAccount userAcc = getLoginUserAccount();
		if (userAcc == null)
		{
			return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
		}
		if (!isPermitted(ModelOperation.READ.name()))
		{
			return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
		}
		BookingOrder bookingOrder = new BookingOrder();
		try {
			bookingOrder = service.generateBookingOrder(clientRequest.getData(),userAcc);
			if(bookingOrder != null){
				return generateRESTFulSuccResponse(bookingOrder,"Generate Booking Success");
			}
		} catch (Exception e) {
			// TODO: handle exception
			return generateErrorRESTFulResponse(e.getMessage());

		}
		return generateRESTFulResponse(bookingOrder);
	}

	@PostMapping("saveSchemeList")
	public @ResponseBody ResponseEntity<ServerResponse<Integer>> saveSchemeList(@RequestBody ClientRequest<List<QuoteScheme>> clientRequest)
	{
		UserAccount userAcc = getLoginUserAccount();
		if (userAcc == null)
		{
			return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
		}
		if (!isPermitted(ModelOperation.CREATE.name()))
		{
			return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
		}
		try
		{
			List<QuoteScheme> quoteSchemeList = clientRequest.getData();
			if (quoteSchemeList != null && quoteSchemeList.size() > 0)
			{
				addOwnerFor(quoteSchemeList, userAcc);
				List<QuoteScheme> addEntities = service.create(quoteSchemeList, userAcc.getId(), CommonConstant.defaultSystemLanguage);
				if(!CollectionUtils.isEmpty(addEntities)){
					return generateRESTFulSuccResponse(addEntities.size(),"Save "+ addEntities.size() +"Quote Scheme Items Success");
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			return generateErrorRESTFulResponse(e.getMessage());
		}
		return generateErrorRESTFulResponse("QuoteSchemeList Num Is 0");
	}
}
