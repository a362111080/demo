package com.zero.egg.service;

import com.zero.egg.requestDTO.OrderAddressDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

/**
 * @Author lyming
 * @Date 2019-08-21 11:24
 **/
public interface OrderAddressService {

    /**
     * 新增收货地址
     * @param orderAddressDTO
     * @return
     * @throws ServiceException
     */
    Message createAddress(OrderAddressDTO orderAddressDTO) throws ServiceException;
}
