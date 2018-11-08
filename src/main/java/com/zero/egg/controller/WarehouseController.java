package com.zero.egg.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.zero.egg.model.Warehouse;
import com.zero.egg.service.WarehouseService;
import com.zero.egg.service.impl.WarehouseServiceImpl;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.PageData;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;
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
	 *@title: addWarehouse
	 *@Description 添加仓库
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addwarehouse")
	public Message addWarehouse(HttpServletRequest request) {
		Message mg = new Message();
		Warehouse warehouse = new Warehouse();
		PageData pd = new PageData(request);
		warehouse.setId(UuidUtil.get32UUID());
		warehouse.setWarehouse_name(pd.getString("warehouse_name"));
		warehouse.setWarehouse_code(pd.getString("warehouse_code"));
		warehouse.setStore_id(pd.getString("store_id"));
		warehouse.setStrdesc(pd.getString("strdesc"));
		warehouse.setLng_state(pd.getInteger("lng_state"));
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
	private Message updateWarehouse(HttpServletRequest request) {
		Message mg = new Message();
		PageData pd = new PageData(request);
		String id = pd.getString("id");
		//判断是否有仓库id
		if (StringUtils.isNotBlank(id)) {
			Warehouse warehouse = new Warehouse();
			warehouse.setId(pd.getString("id"));
			warehouse.setWarehouse_name(pd.getString("warehouse_name"));
			warehouse.setWarehouse_code(pd.getString("warehouse_code"));
			warehouse.setStore_id(pd.getString("store_id"));
			warehouse.setStrdesc(pd.getString("strdesc"));
			warehouse.setLng_state(pd.getInteger("lng_state"));
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
