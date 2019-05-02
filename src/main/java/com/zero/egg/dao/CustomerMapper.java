package com.zero.egg.dao;

import com.zero.egg.model.Customer;
import com.zero.egg.model.city;
import com.zero.egg.requestDTO.CustomerRequestDTO;

import java.util.List;

public interface CustomerMapper {

	int	AddCustomer(Customer model);

	int UpdateCustomer(Customer model);

	List<Customer> GetCustomerList(CustomerRequestDTO model);

    int DeleteCustomer(List<String> ids);

	List<city> GetCitys(city model);
}
