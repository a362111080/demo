package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.OrderAddressMapper;
import com.zero.egg.model.OrderAddress;
import com.zero.egg.requestDTO.OrderAddressDTO;
import com.zero.egg.service.OrderAddressService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderAddressServiceImpl
 * @Author lyming
 * @Date 2019-08-21 11:28
 **/
@Slf4j
@Service
public class OrderAddressServiceImpl implements OrderAddressService {

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Override
    public Message createAddress(OrderAddressDTO orderAddressDTO) throws ServiceException {
        Message message = new Message();
        try {
            OrderAddress orderAddress = new OrderAddress();
            TransferUtil.copyProperties(orderAddress, orderAddressDTO);
            orderAddressMapper.insert(orderAddress);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("createAddress service error:" + e);
            throw new ServiceException("createAddress service error");
        }
    }

    @Override
    public Message updateAddress(OrderAddressDTO orderAddressDTO) throws ServiceException {
        Message message = new Message();
        try {
            OrderAddress orderAddress = new OrderAddress();
            TransferUtil.copyProperties(orderAddress, orderAddressDTO);
            orderAddressMapper.update(orderAddress, new UpdateWrapper<OrderAddress>()
                    .eq("id", orderAddressDTO.getId())
                    .eq("user_id", orderAddressDTO.getUserId()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("updateAddress service error:" + e);
            throw new ServiceException("updateAddress service error");
        }
    }
}
