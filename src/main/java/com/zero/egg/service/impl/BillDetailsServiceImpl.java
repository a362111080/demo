package com.zero.egg.service.impl;

import com.zero.egg.dao.BillMapper;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.service.IBillDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class BillDetailsServiceImpl extends ServiceImpl<BillDetailsMapper, BillDetails> implements IBillDetailsService {

    @Autowired
    private BillDetailsMapper mapper;
    @Override
    public List<BillDetails> getbilldetsils(Bill bill) {
        return  mapper.getbilldetsils(bill);
    }

    @Override
    public Boolean updateDetails(BillDetails iDetails) {

        return  mapper.updateDetails(iDetails);
    }
}
