package com.zero.egg.service;

import java.util.List;

import com.zero.egg.model.Supplier;

public interface SupplierService {
		//新增合作单位
		int AddSupplier(Supplier model);

		int UpdateSupplier(Supplier model);

		List<Supplier> GetSupplierList(Supplier model);
		
}
