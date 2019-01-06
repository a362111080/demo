package com.zero.egg.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.zero.egg.model.Warehouse;
import com.zero.egg.requestDTO.WarehouseRequestDTO;

@Mapper
@Repository
public interface WarehouseMapper {
	
	Warehouse getWarehouseInfoById(String Id);
	
	int addWarehouse(Warehouse warehouse);
	
	int updateWarehouse(Warehouse warehouse);
	
	List<Warehouse> warehouseList(WarehouseRequestDTO warehouse);

}
