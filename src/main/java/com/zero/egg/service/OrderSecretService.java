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
}
