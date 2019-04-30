package com.zero.egg.controller;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.model.Bill;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.IBillService;
import com.zero.egg.tool.StringTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  财务账单控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
@RestController
@Api(value="财务账单")
@RequestMapping("/bill")
public class BillController {
	
	@Autowired
	private IBillService billService;
	
	@LoginToken
	//@PassToken
	@ApiOperation(value="分页查询财务账单")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public ListResponse<Bill> list(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="company"
			,value="企业主键、店铺主键、账单编号（可选）、客户（可选）、供应商（可选）、账单时间范围（默认当月，属于必输项）,状态（1挂账，-1销账）") Bill bill) {
		ListResponse<Bill> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<Bill> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (bill != null) {
			queryWrapper.like(StringUtils.isNotBlank(bill.getCompanyId()),"company_id", bill.getCompanyId())
			.eq(StringUtils.isNotBlank(bill.getShopId()),"shop_id", bill.getShopId())
			.eq(StringUtils.isNotBlank(bill.getBillNo()),"bill_no", bill.getBillNo())
			.eq(StringUtils.isNotBlank(bill.getStatus()),"status", bill.getStatus())
			.eq(StringUtils.isNotBlank(bill.getCussupId()), "cussup_id", bill.getCussupId());
			LocalDateTime today;
			//去本月第一天
			LocalDateTime firstDayOfThisMonth;
			// 取本月最后一天，再也不用计算是28，29，30还是31：
			LocalDateTime lastDayOfThisMonth; 
			if (bill.getBillDate() == null) {
				today = LocalDateTime.now();
				firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth()); 
				lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth()); 
			}else {
				Instant instant = bill.getBillDate().toInstant();
			    ZoneId zone = ZoneId.systemDefault();
			    today = LocalDateTime.ofInstant(instant, zone);
				//去本月第一天
				firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth()); 
				// 取本月最后一天，再也不用计算是28，29，30还是31：
				lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth()); 
			}
			queryWrapper.ge("bill_date", firstDayOfThisMonth);
			queryWrapper.le("bill_date", lastDayOfThisMonth);
		}
		IPage<Bill> list = billService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
	}
	
	@LoginToken
	@ApiOperation(value="批量修改账单状态(挂账/销账)")
	@RequestMapping(value="/batchupdate.do",method=RequestMethod.POST)
	public BaseResponse<Object> batchUpdateStatus(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="ids",value="账单ids,逗号拼接") String ids
			,@ApiParam(required=true,name="status",value="状态：1挂账，-1销账") @RequestParam String status) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		List<String> idsList = StringTool.splitToList(ids, ",");
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (idsList !=null) {
			List<Bill> billList = new ArrayList<>();
			for (String id : idsList) {
				Bill bill = new Bill();
				bill.setId(id);
				bill.setStatus(status);
				bill.setModifier(loginUser.getId());
				bill.setModifytime(new Date());
				billList.add(bill);
			}
			if (billService.updateBatchById(billList)) {//逻辑删除
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("删除成功");
			}
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		
		return response;
	}

}
