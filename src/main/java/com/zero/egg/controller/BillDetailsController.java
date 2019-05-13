package com.zero.egg.controller;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zero.egg.model.UnloadGoods;
import com.zero.egg.tool.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.IBillDetailsService;
import com.zero.egg.service.IBillService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
		List<BillDetails> companyUserList = billDetailsService.getbilldetsils(bill);

		ms.setData(companyUserList);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return ms;
	}
	
	@LoginToken
	@ApiOperation(value="更新账单详情金额")
	@RequestMapping(value="/editAmount",method=RequestMethod.POST)
	public Message<Object> edit(@RequestBody  Bill model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		BigDecimal Amount = BigDecimal.ZERO;
		for (int n = 0; n <= model.getUnloadDetails().size(); n++) {
			BillDetails IDetails = new BillDetails();
			IDetails.setPrice(model.getUnloadDetails().get(n).getPrice());
			IDetails.setQuantity(model.getUnloadDetails().get(n).getQuantity());
			IDetails.setAmount(model.getUnloadDetails().get(n).getPrice().multiply(model.getUnloadDetails().get(n).getQuantity()));
			IDetails.setModifier(user.getId());
			IDetails.setModifytime(new Date());
			Amount = Amount.add(model.getUnloadDetails().get(n).getPrice().multiply(model.getUnloadDetails().get(n).getQuantity()));
			billDetailsService.updateDetails(IDetails);
		}
		//更新总价
		Bill bill = new Bill();
		bill.setId(model.getId());
		bill.setAmount(Amount);
		bill.setModifier(user.getId());
		bill.setModifytime(new Date());
		bill.setRealAmount(model.getRealAmount());
		if (billService.updateById(bill)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		} else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	
	
}
