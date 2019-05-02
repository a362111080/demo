package com.zero.egg.service;

import java.util.List;

import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;

public interface SupplierService {
		//新增合作单位
		int AddSupplier(Supplier model);

		int UpdateSupplier(Supplier model);

		List<Supplier> GetSupplierList(SupplierRequestDTO model);

        int DeleteSupplier(SupplierRequestDTO supplier);
}
