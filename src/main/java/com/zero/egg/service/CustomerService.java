package com.zero.egg.service;

import com.zero.egg.model.Customer;
import com.zero.egg.model.city;
import com.zero.egg.requestDTO.CustomerRequestDTO;

import java.util.List;

public interface CustomerService {
    int AddCustomer(Customer model);

    int UpdateCustomer(Customer model);

    List<Customer> GetCustomerList(CustomerRequestDTO model);

    int DeleteCustomer(CustomerRequestDTO customer);

    List<city> GetCitys(city model);

    /**
     * 查询出货合作商列表(包含零售合作商)
     * @param model
     * @return
     */
    List<Customer> getShipmentSupplierList(CustomerRequestDTO model);
}
