package com.zero.egg.controller;


import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.Bill;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.IBillDetailsService;
import com.zero.egg.service.IBillService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  账单详情控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
@RestController
@Api(value="财务账单详情")
@RequestMapping("/billdetails")
@Slf4j
public class BillDetailsController {

	@Autowired
	private IBillService billService;
	@Autowired
	private IBillDetailsService billDetailsService;
	@Autowired
	private HttpServletRequest request;
	
	@LoginToken
	@ApiOperation(value="根据账单主键查询账单详情")
	@RequestMapping(value="/getbilldetsils",method=RequestMethod.POST)
	public Message getByBillId(@RequestBody  Bill bill) {
		Message ms = new Message();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		try {
			bill.setCompanyId(loginUser.getCompanyId());
			bill.setShopId(loginUser.getShopId());
			ms = billDetailsService.getbilldetsils(bill);
		} catch (Exception e) {
			ms.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			ms.setMessage(UtilConstants.ResponseMsg.FAILED);
			if (!(e instanceof ServiceException)) {
				log.error("getbilldetsils controller error" + e);
			}
		}

		return ms;
	}
	
	@LoginToken
	@ApiOperation(value="更新账单详情金额")
	@RequestMapping(value="/editAmount",method=RequestMethod.POST)
	public Message<Object> edit(@RequestBody  Bill model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		try {
			message = billService.updateBillAndDetails(model, user);
		} catch (Exception e) {
			if (!(e instanceof ServiceException)) {
				log.error("editAmount Controller error" + e);
			}
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	
	
}
