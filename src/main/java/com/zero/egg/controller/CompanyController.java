package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.CompanyEnums;
import com.zero.egg.model.Company;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.CompanyRequest;
import com.zero.egg.requestDTO.CompanyUserRequest;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.responseDTO.CompanyinfoResponseDto;
import com.zero.egg.service.CustomerService;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  企业控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-09
 */
@RestController
@Api(value="企业管理")
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private ICompanyService iCompanyService;
	@Autowired
	private ICompanyUserService iCompanyUserService;
	@Autowired
	private IShopService shopService;
	@Autowired
	private CustomerService CustomerSv;

	@Autowired
	private HttpServletRequest request;
	
	@LoginToken
	@ApiOperation(value="查询企业")
	@RequestMapping(value="/userlist",method=RequestMethod.POST)
	public Message<IPage<Company>> list(HttpServletRequest request,
			@RequestBody @ApiParam(required=false,name="company",value="查询字段：关键词（名称 、编号）、状态") CompanyRequest company) {
		//ListResponse<Company> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<Company>> message = new Message<IPage<Company>>();
		Page<Company> page = new Page<>();
		page.setCurrent(company.getCurrent());
		page.setSize(company.getSize());
		QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (company != null) {
			queryWrapper.like(StringUtils.isNotBlank(company.getName()),"name", company.getName())
			.like(StringUtils.isNotBlank(company.getCode()),"code", company.getCode())
			.eq(StringUtils.isNotBlank(company.getStatus()), "status", company.getStatus());
		}
		IPage<Company> list = iCompanyService.page(page, queryWrapper);
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
		
	}
	
	@LoginToken
	@ApiOperation(value="根据Id查询企业")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public Message<Company> getById(@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Company> message = new Message<Company>();
		Company company = iCompanyService.getById(id);
		if (company != null) {
			message.setData(company);
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="新增企业")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public Message<Object> add(HttpServletRequest request,@RequestBody @ApiParam(required=true,name="company",value="企业信息:编号，名称，电话") Company company
			,HttpSession session) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setModifier(loginUser.getId());
		company.setCreator(loginUser.getId());
		company.setModifytime(new Date());
		company.setCreatetime(new Date());
		if (iCompanyService.save(company)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	

	@LoginToken
	@ApiOperation(value="新增编辑企业、企业账号和店铺")
	@Transactional(rollbackFor=Exception.class)
	@RequestMapping(value="/addcompanyshop",method=RequestMethod.POST)
	public Message<Object> addCompanyAndShop(@RequestBody  CompanyUser companyUser) throws Exception {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		//验证账号、企业名称重名
		String  isExist="N";
		CompanyUserRequest  dto=new CompanyUserRequest();
		dto.setLoginname(companyUser.getLoginname());
		List<CompanyinfoResponseDto> ilist=iCompanyUserService.getCompanyinfolist(dto);
		dto.setLoginname("");
		dto.setCompanyName(companyUser.getCompanyName());
		List<CompanyinfoResponseDto> ilist2=iCompanyUserService.getCompanyinfolist(dto);
		if (ilist.size()>0)
		{
			if (ilist.size()==1 && ilist.get(0).getCompanyId().equals(companyUser.getCompanyId()))
			{
				isExist="Y";
			}
			else
			{
				isExist="N";
				message.setMessage("企业账号已存在，请重新输入");
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			}
		}
		else
		{
			isExist="Y";
		}
		if(ilist2.size()>0 && isExist=="Y")
		{
			if (ilist2.size()==1 && ilist2.get(0).getCompanyId().equals(companyUser.getCompanyId()))
			{
				isExist="Y";
			}
			else
			{
				isExist="N";
				message.setMessage("企业名称已存在，请重新输入");
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			}
		}
		if (isExist=="Y"){
			Company company = new Company();
			String pwd ="";
			if(null!=companyUser.getPassword())
			{
				pwd = MD5Utils.encode(companyUser.getPassword());
			}
			if (null != companyUser.getCompanyId()) {
				company.setId(companyUser.getCompanyId());
				company.setModifier(loginUser.getId());
				company.setModifytime(new Date());
				company.setCreatetime(new Date());
				company.setCreator(loginUser.getId());
				company.setName(companyUser.getCompanyName());
				company.setBegintime(companyUser.getBegintime());
				company.setEndtime(companyUser.getEndtime());
				company.setDr(false);
				if (iCompanyService.updateById(company)) {
					companyUser.setModifier(loginUser.getId());
					companyUser.setCreator(loginUser.getId());
					companyUser.setModifytime(new Date());
					companyUser.setCreatetime(new Date());
					if (pwd!="") {
						companyUser.setPassword(pwd);
					}
					companyUser.setDr(false);
					iCompanyUserService.updateById(companyUser);
					if (companyUser.getShopList() != null && companyUser.getShopList().size() > 0) {
						for (Shop shop : companyUser.getShopList()) {
							if (null != shop.getId()) {
								shop.setCompanyId(companyUser.getCompanyId());
								shop.setModifier(loginUser.getId());
								shop.setCreator(loginUser.getId());
								shopService.updateById(shop);
							} else {
								String  newShopid=UuidUtil.get32UUID();
								shop.setCompanyId(companyUser.getCompanyId());
								shop.setId(newShopid);
								shop.setModifier(loginUser.getId());
								shop.setModifytime(new Date());
								shop.setCreatetime(new Date());
								shop.setCreator(loginUser.getId());
								shop.setDr(false);
								shopService.save(shop);
								//增加对应零售合作商
								Customer  model=new  Customer();
								model.setCreator(loginUser.getId());
								model.setCreatetime(new Date());
								model.setShopid(shop.getId());
								model.setCompanyid(companyUser.getCompanyId());
								model.setModifier(loginUser.getId());
								model.setModifytime(new Date());
								model.setPhone("123456");
								model.setLinkman("系统");
								model.setStatus("");
								model.setCityid("");
								model.setIsRetail(1);
								model.setName("零售");
								model.setShortname("");
								model.setWeightMode("1");
								model.setType("0");
								CustomerSv.AddCustomer(model);
							}
						}
					}
				}
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			} else {
				String companyid = UuidUtil.get32UUID();
				company.setId(companyid);
				company.setModifier(loginUser.getId());
				company.setModifytime(new Date());
				company.setCreatetime(new Date());
				company.setCreator(loginUser.getId());
				company.setName(companyUser.getCompanyName());
				company.setBegintime(companyUser.getBegintime());
				company.setEndtime(companyUser.getEndtime());
				company.setStatus(CompanyEnums.Status.Normal.index().toString());
				company.setDr(false);
				if (iCompanyService.save(company)) {
					companyUser.setId(UuidUtil.get32UUID());
					companyUser.setModifier(loginUser.getId());
					companyUser.setCreator(loginUser.getId());
					companyUser.setCompanyId(company.getId());
					companyUser.setModifytime(new Date());
					if (pwd!="") {
						companyUser.setPassword(pwd);
					}
					companyUser.setCreatetime(new Date());
					companyUser.setStatus(CompanyEnums.Status.Normal.index().toString());
					companyUser.setDr(false);
					iCompanyUserService.save(companyUser);
					if (companyUser.getShopList() != null && companyUser.getShopList().size() > 0) {
						for (Shop shop : companyUser.getShopList()) {
							if (null == shop.getId()) {
								String newShopid = UuidUtil.get32UUID();
								shop.setId(newShopid);
								shop.setCompanyId(company.getId());
								shop.setModifier(loginUser.getId());
								shop.setModifytime(new Date());
								shop.setCreatetime(new Date());
								shop.setCreator(loginUser.getId());
								shop.setDr(false);
								shopService.save(shop);
							}
							else
							{
								shop.setCompanyId(company.getId());
								shop.setModifier(loginUser.getId());
								shop.setModifytime(new Date());
								shop.setCreatetime(new Date());
								shop.setCreator(loginUser.getId());
								shop.setDr(false);
								shopService.updateById(shop);
							}
							//增加对应零售合作商
							Customer  model=new  Customer();
							model.setCreator(loginUser.getId());
							model.setCreatetime(new Date());
							model.setShopid(shop.getId());
							model.setCompanyid(companyUser.getCompanyId());
							model.setModifier(loginUser.getId());
							model.setModifytime(new Date());
							model.setPhone("123456");
							model.setLinkman("系统");
							model.setStatus("");
							model.setCityid("");
;							model.setIsRetail(1);
							model.setName("零售");
							model.setShortname("");
							model.setWeightMode("1");
							model.setType("0");
							CustomerSv.AddCustomer(model);
						}
					}
				}

				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id修改企业信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestBody  @ApiParam(required=true,name="company",value="企业信息：编号，名称，电话") Company company,HttpSession session) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setModifier(loginUser.getId());
		company.setModifytime(new Date());
		if (iCompanyService.updateById(company)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="停用企业")
	@RequestMapping(value="/stopcompany.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		Company company = new Company();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setModifier(loginUser.getId());
		company.setModifytime(new Date());
		company.setStatus(CompanyEnums.Status.Disable.index().toString());
		company.setDr(true);
		company.setId(id);
		if (iCompanyService.updateById(company)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	
	
	@LoginToken
	@ApiOperation(value="根据id删除企业信息")
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public Message<Object> del(@RequestBody Company company) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setDr(true);
		company.setModifier(loginUser.getId());
		company.setModifytime(new Date());
		if (iCompanyService.updateById(company)) {//逻辑删除
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除企业信息")
	@RequestMapping(value="/batchdel",method=RequestMethod.POST)
	public Message<Object> batchDel(@RequestBody Company company) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		List<String> idsList = StringTool.splitToList(company.getIds(), ",");
		if (idsList !=null) {
			List<Company> companyList = new ArrayList<>();
			for (String id : idsList) {
				Company Dcompany = new Company();
				Dcompany.setId(id);
				Dcompany.setDr(true);
				Dcompany.setModifier(loginUser.getId());
				Dcompany.setModifytime(new Date());
				companyList.add(Dcompany);
			}
			if (iCompanyService.updateBatchById(companyList)) {//逻辑删除
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			}
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		
		return message;
	}
	
	
	
}
