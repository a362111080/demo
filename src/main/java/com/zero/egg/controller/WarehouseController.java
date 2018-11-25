package com.zero.egg.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.Warehouse;
import com.zero.egg.service.WarehouseService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.PageDTO;
import com.zero.egg.tool.PageData;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;
import com.zero.egg.tool.UtilConstants.WarehouseState;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * @author hhfeng
 * @Title: WarehouseController 
 * @Description:仓库管理控制器  
 * @date 2018年11月5日
 */
@Api(value="仓库管理")
@RestController
@RequestMapping(value="/warehouse")
public class WarehouseController {

	@Autowired 
	WarehouseService warehouseService;
	
	/**
	 *@title: getWarehouseInfoById
	 *@Description 根据仓库id获取仓库信息
	 * @param request
	 * @param mode
	 * @return
	 */
	@ApiOperation(value="获取仓库信息" ,notes="根据仓库id查询")
	@ApiImplicitParam(paramType="query",name="warehouse_id",value="仓库id",required=true,dataType="String")
	@RequestMapping(value="/getwarehouseinfo" ,method=RequestMethod.GET)
	public Message getWarehouseInfoById(@RequestParam String warehouseId) {
		Warehouse warehouse = warehouseService.getWarehouseInfoById(warehouseId);
		Message ms = new Message();
		ms.setState(ResponseCode.SUCCESS_HEAD);
		ms.setMessage(ResponseMsg.SUCCESS);
		ms.setData(warehouse);
		return ms;
	}
	
	/**
	 *@title: warehouseList
	 *@Description 查询仓库信息列表(包括所有查询)
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value="查询仓库信息列表",notes="分页查询，各种条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="页码",value="pageNum",dataType="int"),
		@ApiImplicitParam(paramType="query",name="页大小",value="pageSize",dataType="int")
	})
	@RequestMapping(value="/warehouselist",method=RequestMethod.POST)
	public Message warehouseList(@RequestParam int pageNum,@RequestParam int pageSize, Warehouse warehouse) {
		Message ms = new Message();
		//Warehouse warehouse = new Warehouse();
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("warehouse_id*1 desc");
		List<Warehouse> list = warehouseService.warehouseList(warehouse);
		PageInfo<Warehouse> pageInfo = new PageInfo<Warehouse>(list);
		ms.setData(pageInfo);
		ms.setState(ResponseCode.SUCCESS_HEAD);
		ms.setMessage(ResponseMsg.SUCCESS);
		return ms;
	}
	
	/**
	 *@title: addWarehouse
	 *@Description 添加仓库
	 * @param request
	 * @return
	 */
	@ApiOperation(value="仓库信息添加",notes="warehouseId,createdate,lngState三个字段内容后台生成")
	@RequestMapping(value="/addwarehouse",method=RequestMethod.POST)
	public Message addWarehouse(@RequestBody Warehouse warehouse) {
		Message mg = new Message();
		warehouse.setWarehouseId(UuidUtil.get32UUID());
		warehouse.setCreatedate(new Date());
		warehouse.setLngState(WarehouseState.enabled);
		int result = warehouseService.addWarehouse(warehouse);
		if (result==1) {
			mg.setData(warehouse);
			mg.setMessage(ResponseMsg.SUCCESS);
			mg.setState(ResponseCode.SUCCESS_HEAD);
		}else {
			mg.setMessage(ResponseMsg.FAILED);
			mg.setState(ResponseCode.EXCEPTION_HEAD);
		}
		return mg;
		
	}
	
	/**
	 *@title: updateWarehouse
	 *@Description 修改仓库信息
	 * @param request
	 * @return
	 */
	@ApiOperation(value="修改仓库信息",notes="warehouseId必填")
	@RequestMapping(value="/updatewarehouse",method=RequestMethod.POST)
	private Message updateWarehouse(@RequestBody Warehouse warehouse) {
		Message mg = new Message();
		String id = warehouse.getWarehouseId();
		//判断是否有仓库id
		if (StringUtils.isNotBlank(id)) {
			int result = warehouseService.updateWarehouse(warehouse);
			if (result==1) {
				mg.setData(warehouse);
				mg.setMessage(ResponseMsg.SUCCESS);
				mg.setState(ResponseCode.SUCCESS_HEAD);
			}else {
				mg.setMessage(ResponseMsg.FAILED);
				mg.setState(ResponseCode.EXCEPTION_HEAD);
			}
		}else {
			mg.setMessage(ResponseMsg.FAILED);
			mg.setMessage(ResponseMsg.PARAM_MISSING);
		}
		return mg;
	}
	
	
	
	
	
}
