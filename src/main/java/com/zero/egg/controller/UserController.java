package com.zero.egg.controller;


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
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.Shop;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.UserRequest;
import com.zero.egg.service.IShopService;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;

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
	@Autowired
	private IShopService shopService;
	
	@LoginToken
	@ApiOperation(value="分页查询员工")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<IPage<User>> list(
			@RequestBody @ApiParam(required=false,name="user",value="查询字段：企业主键，店铺主键，关键词（名称 、编号）、状态") UserRequest user) {
		//ListResponse<User> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<User>> message = new Message<IPage<User>>();
		Page<User> page = new Page<>();
		page.setCurrent(user.getCurrent());
		page.setSize(user.getSize());
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
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		message.setData(list);
		return message;
		
	}
	
	@LoginToken
	@ApiOperation(value="根据Id查询员工")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public Message<User> getById(@RequestParam @ApiParam(required=true,name="id",value="员工id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<User> message = new Message<User>();
		User user = userService.getById(id);
		if (user != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(user);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据条件查询员工")
	@RequestMapping(value="/get-condition.data",method=RequestMethod.POST)
	public Message<List<User>> getByCompanyId(
			@RequestBody @ApiParam(required=false,name="user",value="查询字段：企业主键，店铺主键，关键词（名称 、编号）、状态") User user) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<User>> message = new Message<List<User>>();
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (user != null) {
			queryWrapper.like(StringUtils.isNotBlank(user.getName()),"name", user.getName())
			.like(StringUtils.isNotBlank(user.getCode()),"code", user.getCode())
			.eq(StringUtils.isNotBlank(user.getStatus()), "status", user.getStatus())
			.eq(StringUtils.isNotBlank(user.getCompanyId()),"company_id",user.getCompanyId())
			.eq(StringUtils.isNotBlank(user.getShopId()), "shop_id", user.getShopId());
		}
		List<User> userList = userService.list(queryWrapper);
		if (userList != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(userList);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="新增员工")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public Message<Object> add(
			@RequestBody @ApiParam(required=true,name="user",value="员工信息:店铺主键，企业主键，编号，登录名,名称，电话，性别，密码(不填则默认888888)") User user
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword("888888");
		}
		user.setModifier(loginUser.getId());
		user.setCreator(loginUser.getId());
		String shopId = user.getShopId();
		Shop shop = shopService.getById(shopId);
		if (shop != null) {
			Integer count;
			boolean checkResult = true;
			QueryWrapper<User> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("shop_id", user.getShopId());
			//检查人员类型数量是否有空余
			if (UserEnums.Type.Pc.index().equals(user.getType())) {
				count = shop.getPcClient();
				queryWrapper.eq("type", UserEnums.Type.Pc.index());
				List<User> userList = userService.list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("Pc客户端名额已用完");
					checkResult = false;
				}
			}else if (UserEnums.Type.Boss.index().equals(user.getType())) {
				count = shop.getBossClient();
				queryWrapper.eq("type", UserEnums.Type.Boss.index());
				List<User> userList = userService.list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("Boss客户端名额已用完");
					checkResult = false;
				}
			}else if (UserEnums.Type.Staff.index().equals(user.getType())) {
				count = shop.getStaffClient();
				queryWrapper.eq("type", UserEnums.Type.Staff.index());
				List<User> userList = userService.list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("员工客户端名额已用完");
					checkResult = false;
				}
			}else if (UserEnums.Type.Device.index().equals(user.getType())) {
				count = shop.getDeviceClient();
				queryWrapper.eq("type", UserEnums.Type.Device.index());
				List<User> userList = userService.list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("设备客户端名额已用完");
					checkResult = false;
				}
			}
			if (checkResult) {
				if (userService.save(user)) {
					message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
					message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
				}else {
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage(UtilConstants.ResponseMsg.FAILED);
				}
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("店铺不存在！");
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id修改员工信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(
			@RequestBody  @ApiParam(required=true,name="user",value="员工信息:店铺主键，企业主键，编号，名称，电话，性别") User user
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		user.setModifier(loginUser.getId());
		user.setModifytime(new Date());
		if (userService.updateById(user)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="员工离职")
	@RequestMapping(value="/user-dimission.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="员工id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		User user = new User();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		user.setModifier(loginUser.getId());
		user.setModifytime(new Date());
		user.setStatus(UserEnums.Status.Disable.index().toString());
		user.setId(id);
		if (userService.updateById(user)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id删除员工信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public Message<Object> del(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		User user = new User();
		user.setDr(true);
		user.setId(id);
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		user.setModifier(loginUser.getId());
		user.setModifytime(new Date());
		if (userService.updateById(user)) {//逻辑删除
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除员工信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public Message<Object> batchDel(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="ids",value="员工ids,逗号拼接") String ids) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<User> userList = new ArrayList<>();
			for (String id : idsList) {
				User user = new User();
				user.setDr(true);
				user.setId(id);
				user.setModifier(loginUser.getId());
				user.setModifytime(new Date());
				userList.add(user);
			}
			if (userService.updateBatchById(userList)) {//逻辑删除
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
		}
		
		return message;
	}
}
