package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.OrderAdMapper;
import com.zero.egg.model.OrderAd;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.OrderAdService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderAdServiceImpl
 * @Author lyming
 * @Date 2019/8/28 10:23 上午
 **/
@Service
@Slf4j
public class OrderAdServiceImpl implements OrderAdService {

    @Autowired
    private OrderAdMapper orderAdMapper;

    @Override
    public Message getAdList() throws ServiceException {
        Message msg = new Message();
        try {
            List<OrderAd> orderAdList = orderAdMapper.selectList(new QueryWrapper<OrderAd>()
                    .eq("dr", false));
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            msg.setData(orderAdList);
            return msg;
        } catch (Exception e) {
            log.error("getAdList service error:" + e);
            throw new ServiceException("getAdList service error:" + e);
        }
    }

    @Override
    public int addOrderad(OrderAd model, LoginUser loginUser) {

        model.setId(UuidUtil.get32UUID());
        model.setCreator(loginUser.getId());
        model.setModifier(loginUser.getId());
        model.setCreatetime(new Date());
        model.setModifytime(new Date());
        model.setDr(false);
        return orderAdMapper.addorderad(model);
    }

    @Override
    public int editorderad(OrderAd model, LoginUser loginUser) {
        model.setModifier(loginUser.getId());
        model.setModifytime(new Date());
        return orderAdMapper.editorderad(model);
    }
}
