package com.zero.egg.service;

import com.zero.egg.model.WechatAuth;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

public interface WechatAuthService {

    /**
     * 通过openId查找平台对应的微信账号
     *
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     *
     * @param wechatAuth
     * @return
     * @throws RuntimeException
     */
    Message register(WechatAuth wechatAuth) throws ServiceException;

    /**
     * 查看是否注册了本地账号
     *
     * @param openId
     * @return
     */
    int getCountByOpenId(String openId);

    //TODO bindUser
}
