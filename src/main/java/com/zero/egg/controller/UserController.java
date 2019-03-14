package com.zero.egg.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.User;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  员工控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@RestController
@Api(value="店铺员工管理")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	
	@ApiOperation(value="分页查询员工")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public ListResponse<User> list(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="user",value="查询字段：企业主键，店铺主键，关键词（名称 、编号）、状态") User user) {
		ListResponse<User> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<User> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (user != null) {
			queryWrapper.like(StringUtils.isNotBlank(user.getName()),"name", user.getName())
			.like(StringUtils.isNotBlank(user.getCode()),"code", user.getCode())
			.eq(StringUtils.isNotBlank(user.getStatus()), "status", user.getStatus())
			.eq(StringUtils.isNotBlank(user.getCompanyId()),"company_id",user.getCompanyId())
			.eq(StringUtils.isNotBlank(user.getShopId()), "shop_id", user.getShopId());
		}
		IPage<User> list = userService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	@ApiOperation(value="根据Id查询员工")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public BaseResponse<Object> getById(@RequestParam @ApiParam(required=true,name="id",value="员工id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		User user = userService.getById(id);
		if (user != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(user);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@ApiOperation(value="根据条件查询员工")
	@RequestMapping(value="/get-condition.data",method=RequestMethod.POST)
	public BaseResponse<Object> getByCompanyId(
			@RequestBody @ApiParam(required=false,name="user",value="查询字段：企业主键，店铺主键，关键词（名称 、编号）、状态") User user) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (user != null) {
			queryWrapper.like(StringUtils.isNotBlank(user.getName()),"name", user.getName())
			.like(StringUtils.isNotBlank(user.getCode()),"code", user.getCode())
			.eq(StringUtils.isNotBlank(user.getStatus()), "status", user.getStatus())
			.eq(StringUtils.isNotBlank(user.getCompanyId()),"company_id",user.getCompanyId())
			.eq(StringUtils.isNotBlank(user.getShopId()), "shop_id", user.getShopId());
		}
		List<User> companyUserList = userService.list(queryWrapper);
		if (companyUserList != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(companyUserList);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@ApiOperation(value="新增员工")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public BaseResponse<Object> add(
			@RequestBody @ApiParam(required=true,name="user",value="员工信息:店铺主键，企业主键，编号，名称，电话，性别") User user
			,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		user.setId(UuidUtil.get32UUID());
		user.setCreatetime(LocalDateTime.now());
		user.setModifytime(LocalDateTime.now());
		user.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		/*LoginInfo loginUser = (LoginInfo) session.getAttribute(SysConstants.LOGIN_USER);*/
		user.setPassword("888888");
		user.setModifier("1");
		user.setCreator("1");
		user.setDr(false);
		if (userService.save(user)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("添加成功");
		}
		return response;
	}
	
	@ApiOperation(value="根据id修改员工信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(
			@RequestBody  @ApiParam(required=true,name="user",value="员工信息:店铺主键，企业主键，编号，名称，电话，性别") User user
			,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		if (userService.updateById(user)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	@ApiOperation(value="员工离职")
	@RequestMapping(value="/user-dimission.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(@RequestParam @ApiParam(required=true,name="id",value="员工id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		User user = new User();
		user.setStatus(UserEnums.Status.Disable.index().toString());
		user.setId(id);
		if (userService.updateById(user)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	
	@ApiOperation(value="根据id删除员工信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public BaseResponse<Object> del(@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		User user = new User();
		user.setDr(true);
		user.setId(id);
		if (userService.updateById(user)) {//逻辑删除
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("删除成功");
		}
		return response;
	}
	
	@ApiOperation(value="批量删除员工信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public BaseResponse<Object> batchDel(@RequestParam @ApiParam(required=true,name="ids",value="员工ids,逗号拼接") String ids) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<User> userList = new ArrayList<>();
			for (String id : idsList) {
				User user = new User();
				user.setDr(true);
				user.setId(id);
				userList.add(user);
			}
			if (userService.updateBatchById(userList)) {//逻辑删除
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("删除成功");
			}
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		
		return response;
	}
}
