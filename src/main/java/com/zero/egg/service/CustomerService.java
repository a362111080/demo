package com.zero.egg.service;

import com.zero.egg.model.Customer;

import java.util.List;

public interface CustomerService {
    int AddCustomer(Customer model);

    int UpdateCustomer(Customer model);

    List<Customer> GetCustomerList(Customer model);
}
