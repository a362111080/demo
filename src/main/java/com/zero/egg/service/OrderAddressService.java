package com.zero.egg.service;

import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderAddressDTO;
import com.zero.egg.requestDTO.RemAddressRequestDTO;
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

    /**
     * 修改收货地址
     * @param orderAddressDTO
     * @return
     * @throws ServiceException
     */
    Message updateAddress(OrderAddressDTO orderAddressDTO) throws ServiceException;

    /**
     * 删除用户收货地址
     * @param remAddressRequestDTO
     * @param user
     * @return
     * @throws ServiceException
     */
    Message removeAddress(RemAddressRequestDTO remAddressRequestDTO, LoginUser user) throws ServiceException;

    /**
     * 查询用户收货地址列表(不分页)
     * @param user
     * @return
     * @throws ServiceException
     */
    Message listAddress(LoginUser user) throws ServiceException;
}
