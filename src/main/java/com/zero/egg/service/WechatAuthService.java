package com.zero.egg.service;

import com.zero.egg.model.Shop;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

import java.util.List;

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
     * 注册本平台订货平台用户
     * @param wechatAuth
     * @return
     * @throws ServiceException
     */
    Message<WechatAuth> registerOrderUser(WechatAuth wechatAuth) throws ServiceException;

    /**
     * 查看是否注册了本地账号
     *
     * @param openId
     * @return
     */
    int getCountByOpenId(String openId);


    /**
     * 微信账号和本地账号做绑定(user_id和type)
     *
     * @return
     */
    int bindWechatAuth(WechatAuth wechatAuth);

    /**
     * 查看是否绑定了本地账号
     *
     * @param userId
     * @return
     */
    int getCountByUserId(String userId);

    /**
     * 取消绑定
     * @param openId
     * @return
     * @throws ServiceException
     */
    int cancelBind(String openId) throws ServiceException;

    List<Shop> getSecretBindInfo(WechatAuth wechatAuth) throws ServiceException;
}
