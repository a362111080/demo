package com.zero.egg.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.zero.egg.model.CompanyUser;
import com.zero.egg.requestDTO.CompanyUserRequest;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  企业用户控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-11
 */
@RestController
@Api(value="企业用户管理")
@RequestMapping("/company-user")
public class CompanyUserController {

	@Autowired
	private ICompanyUserService iCompanyUserService;
	
	@LoginToken
	@ApiOperation(value="分页查询企业用户")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<IPage<CompanyUser>> list(
			@RequestBody @ApiParam(required=false,name="companyUser",value="查询字段：关键词（名称 、编号）、状态") CompanyUserRequest companyUser) {
		//ListResponse<CompanyUser> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<CompanyUser>> message = new Message<IPage<CompanyUser>>();
		Page<CompanyUser> page = new Page<>();
		page.setCurrent(companyUser.getCurrent());
		page.setSize(companyUser.getSize());
		QueryWrapper<CompanyUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (companyUser != null) {
			queryWrapper.like(StringUtils.isNotBlank(companyUser.getName()),"name", companyUser.getName())
			.like(StringUtils.isNotBlank(companyUser.getCode()),"code", companyUser.getCode())
			.eq(StringUtils.isNotBlank(companyUser.getStatus()), "status", companyUser.getStatus());
		}
		IPage<CompanyUser> list = iCompanyUserService.page(page, queryWrapper);
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
		
	}
	
	@LoginToken
	@ApiOperation(value="根据Id查询企业用户")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public Message<CompanyUser> getById(@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<CompanyUser> message = new Message<CompanyUser>();
		CompanyUser companyUser = iCompanyUserService.getById(id);
		if (companyUser != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(companyUser);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据企业id查询企业用户")
	@RequestMapping(value="/get-company.data",method=RequestMethod.POST)
	public Message<List<CompanyUser>> getByCompanyId(@RequestParam @ApiParam(required=true,name="companyId",value="企业id") String companyId) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<CompanyUser>> message = new Message<List<CompanyUser>>();
		QueryWrapper<CompanyUser> queryWrapper = new QueryWrapper<CompanyUser>();
		queryWrapper.eq("company_id", companyId);
		queryWrapper.eq("dr", false);
		List<CompanyUser> companyUserList = iCompanyUserService.list(queryWrapper);
		if (companyUserList != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(companyUserList);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="新增企业用户")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public Message<Object> add(HttpServletRequest request
			,@RequestBody @ApiParam(required=true,name="companyUser",value="企业信息:编号，名称，电话，企业主键") CompanyUser companyUser) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		companyUser.setPassword("888888");
		companyUser.setModifier(loginUser.getId());
		companyUser.setCreator(loginUser.getId());
		if (iCompanyUserService.save(companyUser)) {//逻辑删除
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id修改企业用户信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request
			,@RequestBody  @ApiParam(required=true,name="companyUser",value="企业信息：编号，名称，电话，企业主键") CompanyUser companyUser,HttpSession session) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		companyUser.setModifier(loginUser.getId());
		companyUser.setModifytime(new Date());
		if (iCompanyUserService.updateById(companyUser)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	
	
	@LoginToken
	@ApiOperation(value="根据id删除企业用户信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public Message<Object> del(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		CompanyUser companyUser = new CompanyUser();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		companyUser.setModifier(loginUser.getId());
		companyUser.setModifytime(new Date());
		companyUser.setId(id);
		companyUser.setDr(true);
		if (iCompanyUserService.updateById(companyUser)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除企业用户信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public Message<Object> batchDel(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="ids",value="企业ids,逗号拼接") String ids) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		List<String> idsList = StringTool.splitToList(ids, ",");
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (idsList !=null) {
			List<CompanyUser> userList = new ArrayList<>();
			for (String id : idsList) {
				CompanyUser user = new CompanyUser();
				user.setId(id);
				user.setDr(true);
				user.setModifier(loginUser.getId());
				user.setModifytime(new Date());
				userList.add(user);
			}
			if (iCompanyUserService.updateBatchById(userList)) {//逻辑删除
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		
		return message;
	}
}
