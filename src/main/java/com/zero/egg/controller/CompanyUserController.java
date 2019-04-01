package com.zero.egg.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.tool.StringTool;

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
	public ListResponse<CompanyUser> list(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="companyUser",value="查询字段：关键词（名称 、编号）、状态") CompanyUser companyUser) {
		ListResponse<CompanyUser> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<CompanyUser> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<CompanyUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (companyUser != null) {
			queryWrapper.like(StringUtils.isNotBlank(companyUser.getName()),"name", companyUser.getName())
			.like(StringUtils.isNotBlank(companyUser.getCode()),"code", companyUser.getCode())
			.eq(StringUtils.isNotBlank(companyUser.getStatus()), "status", companyUser.getStatus());
		}
		IPage<CompanyUser> list = iCompanyUserService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	@LoginToken
	@ApiOperation(value="根据Id查询企业用户")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public BaseResponse<Object> getById(@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		CompanyUser companyUser = iCompanyUserService.getById(id);
		if (companyUser != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(companyUser);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@LoginToken
	@ApiOperation(value="根据企业id查询企业用户")
	@RequestMapping(value="/get-company.data",method=RequestMethod.POST)
	public BaseResponse<Object> getByCompanyId(@RequestParam @ApiParam(required=true,name="companyId",value="企业id") String companyId) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		QueryWrapper<CompanyUser> queryWrapper = new QueryWrapper<CompanyUser>();
		queryWrapper.eq("company_id", companyId);
		queryWrapper.eq("dr", false);
		List<CompanyUser> companyUserList = iCompanyUserService.list(queryWrapper);
		if (companyUserList != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(companyUserList);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@LoginToken
	@ApiOperation(value="新增企业用户")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public BaseResponse<Object> add(HttpServletRequest request
			,@RequestBody @ApiParam(required=true,name="companyUser",value="企业信息:编号，名称，电话，企业主键") CompanyUser companyUser) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		companyUser.setPassword("888888");
		companyUser.setModifier(loginUser.getId());
		companyUser.setCreator(loginUser.getId());
		if (iCompanyUserService.save(companyUser)) {//逻辑删除
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("添加成功");
		}
		return response;
	}
	
	@LoginToken
	@ApiOperation(value="根据id修改企业用户信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(HttpServletRequest request
			,@RequestBody  @ApiParam(required=true,name="companyUser",value="企业信息：编号，名称，电话，企业主键") CompanyUser companyUser,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		companyUser.setModifier(loginUser.getId());
		companyUser.setModifytime(LocalDateTime.now());
		if (iCompanyUserService.updateById(companyUser)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	
	
	@LoginToken
	@ApiOperation(value="根据id删除企业用户信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public BaseResponse<Object> del(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		CompanyUser companyUser = new CompanyUser();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		companyUser.setModifier(loginUser.getId());
		companyUser.setModifytime(LocalDateTime.now());
		companyUser.setId(id);
		companyUser.setDr(true);
		if (iCompanyUserService.updateById(companyUser)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("删除成功");
		}
		return response;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除企业用户信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public BaseResponse<Object> batchDel(HttpServletRequest request
			,@RequestParam @ApiParam(required=true,name="ids",value="企业ids,逗号拼接") String ids) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
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
				user.setModifytime(LocalDateTime.now());
				userList.add(user);
			}
			if (iCompanyUserService.updateBatchById(userList)) {//逻辑删除
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("删除成功");
			}
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		
		return response;
	}
}
