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
import com.zero.egg.enums.ShopEnums;
import com.zero.egg.model.Shop;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UuidUtil;

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
	
	@ApiOperation(value="分页查询店铺")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public ListResponse<Shop> list(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="shop",value="查询字段：关键词（名称 、编号）、状态") Shop shop) {
		ListResponse<Shop> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<Shop> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (shop != null) {
			queryWrapper.like(StringUtils.isNotBlank(shop.getName()),"name", shop.getName())
			.like(StringUtils.isNotBlank(shop.getCode()),"code", shop.getCode())
			.eq(StringUtils.isNotBlank(shop.getStatus()), "status", shop.getStatus());
		}
		IPage<Shop> list = shopService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	@ApiOperation(value="根据Id查询店铺")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public BaseResponse<Object> getById(@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Shop shop = shopService.getById(id);
		if (shop != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(shop);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@ApiOperation(value="根据企业id查询店铺")
	@RequestMapping(value="/get-company.data",method=RequestMethod.POST)
	public BaseResponse<Object> getByCompanyId(@RequestParam @ApiParam(required=true,name="companyId",value="企业id") String companyId) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		QueryWrapper<Shop> queryWrapper = new QueryWrapper<Shop>();
		queryWrapper.eq("company_id", companyId);
		List<Shop> companyUserList = shopService.list(queryWrapper);
		if (companyUserList != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(companyUserList);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@ApiOperation(value="新增店铺")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public BaseResponse<Object> add(@RequestBody @ApiParam(required=true,name="shop",value="店铺信息:编号，名称，电话，企业主键") Shop shop
			,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		shop.setId(UuidUtil.get32UUID());
		shop.setCreatetime(LocalDateTime.now());
		shop.setModifytime(LocalDateTime.now());
		shop.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		/*LoginInfo loginUser = (LoginInfo) session.getAttribute(SysConstants.LOGIN_USER);*/
		shop.setModifier("1");
		shop.setCreator("1");
		shop.setDr(false);
		if (shopService.save(shop)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("添加成功");
		}
		return response;
	}
	
	@ApiOperation(value="根据id修改店铺信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(@RequestBody  @ApiParam(required=true,name="shop",value="店铺信息：编号，名称，电话，企业主键") Shop shop,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		if (shopService.updateById(shop)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	@ApiOperation(value="停用店铺")
	@RequestMapping(value="/stopshop.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Shop shop = new Shop();
		shop.setStatus(ShopEnums.Status.Disable.index().toString());
		shop.setId(id);
		if (shopService.updateById(shop)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	
	@ApiOperation(value="根据id删除店铺信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public BaseResponse<Object> del(@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Shop shop = new Shop();
		shop.setDr(true);
		shop.setId(id);
		if (shopService.updateById(shop)) {//逻辑删除
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("删除成功");
		}
		return response;
	}
	
	@ApiOperation(value="批量删除店铺信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public BaseResponse<Object> batchDel(@RequestParam @ApiParam(required=true,name="ids",value="店铺ids,逗号拼接") String ids) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<Shop> shopList = new ArrayList<>();
			for (String id : idsList) {
				Shop shop = new Shop();
				shop.setDr(true);
				shop.setId(id);
				shopList.add(shop);
			}
			if (shopService.updateBatchById(shopList)) {//逻辑删除
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("删除成功");
			}
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		
		return response;
	}

}
