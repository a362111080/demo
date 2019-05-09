package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.BillRequest;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.responseDTO.BillReport;
import com.zero.egg.responseDTO.CategorySum;
import com.zero.egg.service.IBillService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	@Autowired
	private HttpServletRequest request;
	
	@LoginToken
	//@PassToken
	@ApiOperation(value="分页查询财务账单（不可用）")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<IPage<Bill>> list(
			@RequestBody @ApiParam(required=false,name="bill"
			,value="企业主键、店铺主键、账单编号（可选）、客户（可选）、供应商（可选）、账单时间范围（默认当月，属于必输项）,状态（1挂账，-1销账）") BillRequest bill) {
		//ListResponse<Bill> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<Bill>> message = new Message<IPage<Bill>>();
		Page<Bill> page = new Page<>();
		page.setCurrent(bill.getCurrent());
		page.setSize(bill.getSize());
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
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量修改账单状态(挂账/销账)（不可用）")
	@RequestMapping(value="/batchupdate.do",method=RequestMethod.POST)
	public Message<Object> batchUpdateStatus(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="ids",value="账单ids,逗号拼接") String ids
			,@ApiParam(required=true,name="status",value="状态：1挂账，-1销账") @RequestParam String status) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
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
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			}
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		
		return message;
	}

	@LoginToken
	@ApiOperation(value = "查询供应商列表（账单）", notes = "往来交易为条件")
	@RequestMapping(value = "/getsupplierlist", method = RequestMethod.POST)
	public Message GetSupplierList(@RequestBody SupplierRequestDTO model) {
		Message ms = new Message();
		//当前登录用户
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(user.getShopId());
		model.setCompanyId(user.getCompanyId());
		PageHelper.startPage(1, 999);
		List<Supplier> Supplier = billService.GetSupplierList(model);
		PageInfo<Supplier> pageInfo = new PageInfo<>(Supplier);
		ms.setData(pageInfo);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return ms;
	}
	@LoginToken
	@ApiOperation(value="查询客户列表（账单）",notes = "往来交易为条件")
	@RequestMapping(value = "/getcustomerlist",method = RequestMethod.POST)
	public Message GetSupplierList(@RequestBody CustomerRequestDTO model) {
		Message ms = new Message();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(user.getShopId());
		model.setCompanyId(user.getCompanyId());
		PageHelper.startPage(1, 999);
		List<Customer> Customer=billService.GetCustomerList(model);
		PageInfo<Customer> pageInfo = new PageInfo<>(Customer);
		ms.setData(pageInfo);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return  ms;
	}
	@LoginToken
	@ApiOperation(value="查询账单列表",notes = "含多条件查询")
	@RequestMapping(value = "/getBilllist",method = RequestMethod.POST)
	public Message GetBillList(@RequestBody BillRequest model) {
		Message ms = new Message();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(user.getShopId());
		model.setCompanyId(user.getCompanyId());
		PageHelper.startPage(model.getCurrent().intValue(),model.getSize().intValue());
		List<Bill> BillList=billService.getBilllist(model);
		PageInfo<Bill> pageInfo = new PageInfo<>(BillList);
		ms.setData(pageInfo);

        BillReport  report=new BillReport();
        for (int i=0;i<BillList.size();i++)
        {
            if (BillList.get(i).getDr())
            {
                report.setCompleteCount(report.getCompleteCount().add(BigDecimal.ONE));
            }
            else
            {
                report.setUnCompleteCount(report.getUnCompleteCount().add(BigDecimal.ONE));
            }
            if (BillList.get(i).getType().equals(TaskEnums.Type.Unload.index().toString()))
            {
                //支出
                report.setInCount(report.getInCount().add(BillList.get(i).getAmount()));
                //进货
                List<CategorySum> CategorySum=billService.getBillCategorySum(BillList.get(i).getId());
                if (report.getInCategorySum()==null) {
                    report.setInCategorySum(CategorySum);
                }
                else {
                    for (int n = 0; n < CategorySum.size(); n++) {
                        boolean exists = false;
                        Integer index = 0;
                        for (int m = 0; m < report.getInCategorySum().size(); m++) {
                            if (report.getInCategorySum().get(m).getGoodsCategoryId() == CategorySum.get(n).getGoodsCategoryId()) {
                                exists = true;
                                index = m;
                                break;
                            } else {
                                exists = false;
                            }
                        }
                        if (exists) {
                            //如果集合中已存在  加上数量
                            report.getInCategorySum().get(index).setCountNum(report.getInCategorySum().get(index).getCountNum().add(CategorySum.get(n).getCountNum()));
                        } else {
                            //如果集合不存在，直接加入集合
                            report.getInCategorySum().add(CategorySum.get(n));
                        }

                    }
                }
            }
            if (BillList.get(i).getType().equals(TaskEnums.Type.Shipment.index().toString())) {
                //收入
                report.setOutCount(report.getOutCount().add(BillList.get(i).getAmount()));
                //出货
                List<CategorySum> CategorySum = billService.getBillCategorySum(BillList.get(i).getId());
                if (report.getOutCategorySum() == null) {
                    report.setOutCategorySum(CategorySum);
                } else {
                    for (int n = 0; n < CategorySum.size(); n++) {
                        boolean exists = false;
                        Integer index = 0;
                        if (report.getOutCategorySum() != null) {
                            for (int m = 0; m < report.getOutCategorySum().size(); m++) {
                                if (report.getOutCategorySum().get(m).getGoodsCategoryId() == CategorySum.get(n).getGoodsCategoryId()) {
                                    exists = true;
                                    index = m;
                                    break;
                                } else {
                                    exists = false;
                                }
                            }
                        }
                        if (exists) {
                            //如果集合中已存在  加上数量
                            report.getOutCategorySum().get(index).setCountNum(report.getOutCategorySum().get(index).getCountNum().add(CategorySum.get(n).getCountNum()));
                        } else {
                            //如果集合不存在，直接加入集合
                            report.getOutCategorySum().add(CategorySum.get(n));
                        }

                    }
                }
            }
        }
        ms.setTotaldata(report);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return  ms;
	}

}
