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


/**
 * @author hhfeng
 * @Title: WarehouseController 
 * @Description:仓库管理控制器  
 * @date 2018年11月5日
 */
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
	@RequestMapping(value="/getwarehouseinfo")
	public Message getWarehouseInfoById(HttpServletRequest request) {
		String id = request.getParameter("id");
		Warehouse warehouse = warehouseService.getWarehouseInfoById(id);
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
	@RequestMapping(value="/warehouselist")
	public Message warehouseList(int pageNum,int pageSize) {
		Message ms = new Message();
		Warehouse warehouse = new Warehouse();
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
	@RequestMapping(value="/addwarehouse")
	public Message addWarehouse(@RequestBody Warehouse warehouse,HttpServletRequest request) {
		Message mg = new Message();
		warehouse.setWarehouse_id(UuidUtil.get32UUID());
		warehouse.setCreatedate(new Date());
		warehouse.setLng_state(WarehouseState.enabled);
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
	@RequestMapping(value="/updatewarehouse")
	private Message updateWarehouse(@RequestBody Warehouse warehouse, HttpServletRequest request) {
		Message mg = new Message();
		PageData pd = new PageData(request);
		String id = pd.getString("id");
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
