package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.SupplierMapper;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper mapper;

    @Override
    public int AddSupplier(Supplier model) {
        return mapper.insert(model);
    }

    @Override
    public int UpdateSupplier(Supplier model) {
        return mapper.UpdateSupplier(model);
    }

    @Override
    public List<Supplier> GetSupplierList(SupplierRequestDTO model) {
        return mapper.GetSupplierList(model);
    }

    @Override
    public int DeleteSupplier(SupplierRequestDTO supplier) {
        return mapper.DeleteSupplier(supplier.getIds());
    }

    @Override
    public int queryCountByCode(String code) {
        int count = mapper.selectCount(new QueryWrapper<Supplier>()
                .eq("code", code));
        return count;
    }

}
