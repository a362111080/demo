package com.zero.egg.service;

import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

/**
 * @author lym
 */
public interface OrderSecretService {
    /**
     * 订货平台用户绑定店铺秘钥
     * @param user
     * @return
     */
    Message bindSecret(String secret,LoginUser user) throws ServiceException;

    /**
     * 获取订货平台用户绑定的店铺列表
     * @param user
     * @return
     * @throws ServiceException
     */
    Message getShopList(LoginUser user)throws ServiceException;
}
