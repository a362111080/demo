package com.zero.egg.dao;

import java.util.List;

import com.zero.egg.model.Supplier;

public interface SupplierMapper {

	int	AddSupplier(Supplier model);

	int UpdateSupplier(Supplier model);

	List<Supplier> GetSupplierList(Supplier model);
}
