package com.zero.egg.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.zero.egg.enums.CompanyEnums;
import com.zero.egg.model.Company;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.CompanyRequest;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
	
	@LoginToken
	@ApiOperation(value="查询企业")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
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
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setModifier(loginUser.getId());
		company.setCreator(loginUser.getId());
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
	@ApiOperation(value="新增企业、企业账号和店铺")
	@Transactional(rollbackFor=Exception.class)
	@RequestMapping(value="/add-company-shop.do",method=RequestMethod.POST)
	public Message<Object> addCompanyAndShop(HttpServletRequest request
			,@RequestBody @ApiParam(required=true,name="company",value="企业信息:编号，名称，电话") Company company
			,@RequestBody @ApiParam(required=true,name="companyUser",value="企业用户：企业信息:编号，名称，电话，企业主键，登录名,密码") CompanyUser companyUser
			,@RequestParam @ApiParam(required=true,name="shopList",value="店铺数组,店铺信息：编号，名称，电话，企业主键，pc端数量,boss端数量,员工端数量，设备端数量，业务员，实施员") List<Shop> shopList
			) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setModifier(loginUser.getId());
		company.setModifytime(new Date());
		company.setModifier(loginUser.getId());
		company.setCreator(loginUser.getId());
		if (iCompanyService.save(company)) {
			if (companyUser != null) {
				companyUser.setModifier(loginUser.getId());
				companyUser.setCreator(loginUser.getId());
				iCompanyUserService.save(companyUser);	
			}
			if (shopList != null && shopList.size()>0) {
				for (Shop shop : shopList) {
					shop.setModifier(loginUser.getId());
					shop.setCreator(loginUser.getId());
					shopService.save(shop);
				}
			}
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
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
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public Message<Object> del(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		Company company = new Company();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		company.setId(id);
		company.setDr(true);
		company.setModifier(loginUser.getId());
		company.setModifytime(new Date());
		if (iCompanyService.updateById(company)) {//逻辑删除
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除企业信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public Message<Object> batchDel(HttpServletRequest request,
			@RequestParam @ApiParam(required=true,name="ids",value="企业ids,逗号拼接") String ids) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<Company> companyList = new ArrayList<>();
			for (String id : idsList) {
				Company company = new Company();
				company.setId(id);
				company.setDr(true);
				company.setModifier(loginUser.getId());
				company.setModifytime(new Date());
				companyList.add(company);
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
