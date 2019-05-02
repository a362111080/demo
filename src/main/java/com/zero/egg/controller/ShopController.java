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
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.ShopEnums;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.ShopRequest;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@RestController
@Api(value="店铺管理")
@RequestMapping("/shop")
public class ShopController {
	
	
	@Autowired
	private IShopService shopService;
	
	@LoginToken
	//@PassToken
	@ApiOperation(value="分页查询店铺")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<IPage<Shop>> list(
			@RequestBody @ApiParam(required=false,name="shop",value="查询字段：关键词（名称 、编号）、状态") ShopRequest shop) {
		//ListResponse<Shop> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<Shop>> message = new Message<IPage<Shop>>();
		Page<Shop> page = new Page<>();
		page.setCurrent(shop.getCurrent());
		page.setSize(shop.getSize());
		QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (shop != null) {
			queryWrapper.like(StringUtils.isNotBlank(shop.getName()),"name", shop.getName())
			.like(StringUtils.isNotBlank(shop.getCode()),"code", shop.getCode())
			.eq(StringUtils.isNotBlank(shop.getStatus()), "status", shop.getStatus());
		}
		IPage<Shop> list = shopService.page(page, queryWrapper);
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
		
	}
	
	@LoginToken
	@ApiOperation(value="根据Id查询店铺")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public Message<Shop> getById(@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Shop> message = new Message<Shop>();
		Shop shop = shopService.getById(id);
		if (shop != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(shop);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据企业id查询店铺")
	@RequestMapping(value="/get-company.data",method=RequestMethod.POST)
	public Message<List<Shop>> getByCompanyId(@RequestParam @ApiParam(required=true,name="companyId",value="企业id") String companyId) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<Shop>> message = new Message<List<Shop>>();
		QueryWrapper<Shop> queryWrapper = new QueryWrapper<Shop>();
		queryWrapper.eq("company_id", companyId);
		queryWrapper.eq("dr", false);
		List<Shop> shopList = shopService.list(queryWrapper);
		if (shopList != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(shopList);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="新增店铺")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public Message<Object> add(@RequestBody @ApiParam(required=true,name="shop",value="店铺信息:编号，名称，地址，联系电话，企业主键，pc端数量,boss端数量,员工端数量，设备端数量，业务员，实施员") Shop shop
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setCreator(loginUser.getId());
		if (shopService.save(shop)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id修改店铺信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestBody  @ApiParam(required=true,name="shop",value="店铺信息：编号，名称，电话，地址，企业主键") Shop shop,HttpSession session) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setModifytime(new Date());
		if (shopService.updateById(shop)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="停用店铺")
	@RequestMapping(value="/stopshop.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		Shop shop = new Shop();
		shop.setStatus(ShopEnums.Status.Disable.index().toString());
		shop.setId(id);
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setModifytime(new Date());
		if (shopService.updateById(shop)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="根据id删除店铺信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public Message<Object> del(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		Shop shop = new Shop();
		shop.setDr(true);
		shop.setId(id);
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setModifytime(new Date());
		if (shopService.updateById(shop)) {//逻辑删除
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="批量删除店铺信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public Message<Object> batchDel(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="ids",value="店铺ids,逗号拼接") String ids) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<Shop> shopList = new ArrayList<>();
			for (String id : idsList) {
				Shop shop = new Shop();
				shop.setDr(true);
				shop.setId(id);
				shop.setModifier(loginUser.getId());
				shop.setModifytime(new Date());
				shopList.add(shop);
			}
			if (shopService.updateBatchById(shopList)) {//逻辑删除
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		
		return message;
	}

}
