package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Customer;
import com.zero.egg.model.city;
import com.zero.egg.requestDTO.CustomerRequestDTO;

import java.util.List;

public interface CustomerMapper extends BaseMapper<Customer> {

	int	AddCustomer(Customer model);

	int UpdateCustomer(Customer model);

	List<Customer> GetCustomerList(CustomerRequestDTO model);

	List<Customer> getShipmentSupplierList(CustomerRequestDTO model);

    int DeleteCustomer(List<String> ids);

	List<city> GetCitys(city model);
}
