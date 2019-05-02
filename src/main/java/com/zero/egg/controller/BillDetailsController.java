package com.zero.egg.controller;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
@RequestMapping("/bill-details")
public class BillDetailsController {

	@Autowired
	private IBillService billService;
	@Autowired
	private IBillDetailsService billDetailsService;
	
	@LoginToken
	@ApiOperation(value="根据账单主键查询账单详情")
	@RequestMapping(value="/get-billid.data",method=RequestMethod.POST)
	public Message<List<BillDetails>> getByBillId(@RequestParam @ApiParam(required=true,name="bill_id",value="账单id") String billId) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<BillDetails>> message = new Message<List<BillDetails>>();
		QueryWrapper<BillDetails> queryWrapper = new QueryWrapper<BillDetails>();
		queryWrapper.eq("bill_id", billId);
		queryWrapper.eq("dr", false);
		List<BillDetails> companyUserList = billDetailsService.list(queryWrapper);
		message.setData(companyUserList);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="更新账单详情金额")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,
			@RequestBody  @ApiParam(required=true,name="billDetails",value="账单主键、账单详情参数集合（账单明细主键、金额）") BillDetails billDetails,HttpSession session) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (billDetailsService.updateById(billDetails)) {
			Bill bill = new Bill();
			bill.setId(billDetails.getBillId());
			bill.setAmount(billDetails.getAmount());
			bill.setModifier(loginUser.getId());
			bill.setModifytime(new Date());
			if (billService.updateById(bill)) {
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}
		return message;
	}
	
	
	
}
