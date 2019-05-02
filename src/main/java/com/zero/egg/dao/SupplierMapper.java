package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;

import java.util.List;

public interface SupplierMapper extends BaseMapper<Supplier> {

	int	AddSupplier(Supplier model);

	int UpdateSupplier(Supplier model);

	List<Supplier> GetSupplierList(SupplierRequestDTO model);

	int DeleteSupplier(List<String> ids);
}
