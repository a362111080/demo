package com.zero.egg.dao;

import java.util.Date;
import java.util.List;

import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;

public interface SupplierMapper {

	int	AddSupplier(Supplier model);

	int UpdateSupplier(Supplier model);

	List<Supplier> GetSupplierList(SupplierRequestDTO model);

	int DeleteSupplier(List<String> ids);
}
