package com.zero.egg.dao;

import com.zero.egg.model.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.SupplierRequestDTO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
public interface BillMapper extends BaseMapper<Bill> {

    List<Supplier> GetSupplierList(SupplierRequestDTO model);

    List<Customer> GetCustomerList(CustomerRequestDTO model);

}
