package com.zero.egg.service.impl;

import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.model.Customer;
import com.zero.egg.model.city;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

;

/**
 * @ClassName SupplierServiceImpl
 * @Description 合作单位模块ServiceImpl
 * @Author CQ
 * @Date 2018/11/7 
 **/
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
    private CustomerMapper mapper;
	
	@Override
	public int AddCustomer(Customer model) {
		return  mapper.AddCustomer(model);
	}

	@Override
	public int UpdateCustomer(Customer model) {
		return  mapper.UpdateCustomer(model);
	}

	@Override
	public List<Customer> GetCustomerList(CustomerRequestDTO model) {
		return  mapper.GetCustomerList(model);
	}

	@Override
	public int DeleteCustomer(CustomerRequestDTO customer) {
		return mapper.DeleteCustomer(customer.getIds());
	}

	@Override
	public List<city> GetCitys(city model) {
		return  mapper.GetCitys(model);
	}
}
