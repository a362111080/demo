package com.zero.egg.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.github.pagehelper.PageHelper;
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
	 *@title: warehouseList
	 *@Description 查询仓库信息列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/warehouselist")
	public Message warehouseList(int pageNum,int pageSize) {
		Message ms = new Message();
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("id*1 desc");
		List<Warehouse> list = warehouseService.warehouseList();
		ms.setData(list);
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
		PageData pd = new PageData(request);
		warehouse.setId(UuidUtil.get32UUID());
		warehouse.setCreatedate(new Date());
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
