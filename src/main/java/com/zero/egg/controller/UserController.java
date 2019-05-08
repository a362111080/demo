package com.zero.egg.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.UserRequest;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.Message;
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
	
	@LoginToken
	@ApiOperation(value="分页查询员工")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<IPage<User>> list(
			@RequestBody @ApiParam(required=false,name="user",value="查询字段：可选（名称 、编号、状态）") UserRequest user
			,HttpServletRequest request) {
		//ListResponse<User> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<User>> message = new Message<IPage<User>>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message = userService.listPage(user, loginUser);
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
			@RequestBody @ApiParam(required=false,name="user",value="查询字段：可选（名称 、编号、状态）") UserRequest user
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<User>> message = new Message<List<User>>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message = userService.listAll(user, loginUser);
		return message;
	}
	
	
	@LoginToken
	@ApiOperation(value="新增员工")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public Message<Object> add(
			@RequestBody @ApiParam(required=true,name="user",value="员工信息:（必填）编号，（必填）登录名,（必填）名称，（必填）电话，（必填）性别，（选填）密码(不填则默认888888)") User user
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message = userService.save(user, loginUser);
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id修改员工信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(
			@RequestBody  @ApiParam(required=true,name="user",value="员工信息:（必填）员工主键，（选填）编号，（选填）名称，（选填）电话，（选填）性别") User user
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message =userService.updateById(user, loginUser);
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="员工离职")
	@RequestMapping(value="/user-dimission.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="员工id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		User user = new User();
		user.setId(id);
		message =userService.updateById(user, loginUser);
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id删除员工信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public Message<Object> del(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="员工id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message =userService.deleteById(id,loginUser);
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除员工信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public Message<Object> batchDel(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="ids",value="员工ids,逗号拼接") String ids) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message =userService.deleteBatchById(ids, loginUser);
		return message;
	}
}
