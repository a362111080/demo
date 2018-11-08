package com.zero.egg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.WarehouseMapper;
import com.zero.egg.model.Warehouse;
import com.zero.egg.service.WarehouseService;

/**
 * @author hhfeng
 * @Title: WarehouseServiceImpl 
 * @Description: 仓库管理service 
 * @date 2018年11月5日
 */
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService{

	@Autowired
	private WarehouseMapper warehouseMapper;
	

	@Override
	public Warehouse getWarehouseInfoById(String Id) {
		return warehouseMapper.getWarehouseInfoById(Id);
	}

	@Override
	public int addWarehouse(Warehouse warehouse) {
		return warehouseMapper.addWarehouse(warehouse);
	}

	@Override
	public int updateWarehouse(Warehouse warehouse) {
		return warehouseMapper.updateWarehouse(warehouse);
	}
	
	
}
