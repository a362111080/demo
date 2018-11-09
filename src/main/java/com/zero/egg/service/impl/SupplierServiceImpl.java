package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.SupplierMapper;
import com.zero.egg.model.Supplier;
import com.zero.egg.service.SupplierService;;

/**
 * @ClassName SupplierServiceImpl
 * @Description 合作单位模块ServiceImpl
 * @Author CQ
 * @Date 2018/11/7 
 **/
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	@Autowired
    private SupplierMapper mapper;
	
	@Override
	public int AddSupplier(Supplier model) {
		return  mapper.AddSupplier(model);
	}

	@Override
	public int UpdateSupplier(Supplier model) {
		return  mapper.UpdateSupplier(model);
	}

	@Override
	public List<Supplier> GetSupplierList(Supplier model) {
		return  mapper.GetSupplierList(model);
	}

}
