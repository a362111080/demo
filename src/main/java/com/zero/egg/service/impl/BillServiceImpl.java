package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.model.Bill;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {


    @Autowired
    private BillMapper mapper;

    @Override
    public List<Supplier> GetSupplierList(SupplierRequestDTO model) {
        return mapper.GetSupplierList(model);
    }

    @Override
    public List<Customer> GetCustomerList(CustomerRequestDTO model) {
        return mapper.GetCustomerList(model);

    }
}
