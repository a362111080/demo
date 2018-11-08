package com.zero.egg.service;

import com.zero.egg.model.Warehouse;

/**
 * @author hhfeng
 * @Title: WarehouseService 
 * @Description:  仓库管理接口
 * @date 2018年11月5日
 */
public interface WarehouseService {

	/**
	 * 根据id查询仓库信息
	 *@title: getWarehouseInfoById
	 *@Description
	 * @param Id
	 * @return
	 */
	Warehouse getWarehouseInfoById(String Id);
	
	/**
	 * 添加仓库
	 *@title: addWarehouse
	 *@Description
	 * @param warehouse
	 * @return
	 */
	int addWarehouse(Warehouse warehouse);
	
	/**
	 * 根据id更新仓库信息
	 *@title: updateWarehouse
	 *@Description
	 * @param warehouse
	 * @return
	 */
	int updateWarehouse(Warehouse warehouse);
}
