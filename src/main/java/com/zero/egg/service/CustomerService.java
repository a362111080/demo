package com.zero.egg.service;

import com.zero.egg.model.Customer;
import com.zero.egg.requestDTO.CustomerRequestDTO;

import java.util.List;

public interface CustomerService {
    int AddCustomer(Customer model);

    int UpdateCustomer(Customer model);

    List<Customer> GetCustomerList(Customer model);

    int DeleteCustomer(CustomerRequestDTO customer);
}
