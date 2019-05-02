package com.zero.egg.service;

import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;

import java.util.List;

public interface SupplierService {
    //新增合作单位
    int AddSupplier(Supplier model);

    int UpdateSupplier(Supplier model);

    List<Supplier> GetSupplierList(SupplierRequestDTO model);

    int DeleteSupplier(SupplierRequestDTO supplier);

    /**
     * 查询生成的code是否被占用
     *
     * @param code
     * @return
     */
    int queryCountByCode(String code);
}
