package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.OrderSecretMapper;
import com.zero.egg.dao.OrderUserSecretMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.dao.UserMapper;
import com.zero.egg.dao.WechatAuthMapper;
import com.zero.egg.enums.WechatAuthStateEnum;
import com.zero.egg.model.OrderSecret;
import com.zero.egg.model.OrderUserSecret;
import com.zero.egg.model.Shop;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName WechatAuthServiceImpl
 * @Author lyming
 * @Date 2019/4/25 15:39
 **/
@Slf4j
@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    @Autowired
    private WechatAuthMapper wechatAuthMapper;

    @Autowired
    private OrderUserSecretMapper orderUserSecretMapper;

    @Autowired
    private OrderSecretMapper orderSecretMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthMapper.selectOne(new QueryWrapper<WechatAuth>()
                .eq("openid", openId));
    }

    @Override
    public Message register(WechatAuth wechatAuth) throws ServiceException {
        Message message = null;
        //空值判断
        if (wechatAuth == null || wechatAuth.getOpenid() == null) {
            message = new Message();
            message.setState(WechatAuthStateEnum.LOGINFAIL.getState());
            message.setMessage(WechatAuthStateEnum.NULL_AUTH_INFO.getStateInfo());
            return message;
        }
        //如果微信账号里夹带着用户信息并且用户id为空,则认为该用户第一次使用平台,且使用微信登录
        //自动创建用户信息
        try {
            wechatAuth.setCreatetime(new Date());
            //创建专属于本平台的微信账号
            int effectedNum = wechatAuthMapper.insert(wechatAuth);
            if (effectedNum <= 0) {
                message = new Message();
                message.setState(WechatAuthStateEnum.REGISTERFAIL.getState());
                message.setMessage(WechatAuthStateEnum.REGISTERFAIL.getStateInfo());
                return message;
            } else {
                message = new Message();
                message.setState(WechatAuthStateEnum.SUCCESS.getState());
                message.setMessage(WechatAuthStateEnum.SUCCESS.getStateInfo());
                return message;
            }
        } catch (Exception e) {
            log.error("insertWechatAuth error:" + e.toString());
            throw new RuntimeException("insertWechatAuth error: "
                    + e.getMessage());
        }
    }

    @Override
    public int getCountByOpenId(String openId) {
        return wechatAuthMapper.selectCount(new QueryWrapper<WechatAuth>()
                .eq("openid", openId));
    }

    @Override
    public int bindWechatAuth(WechatAuth wechatAuth) {
        try {
            wechatAuth.setModifytime(new Date());
            int effectNum = wechatAuthMapper.update(wechatAuth, new UpdateWrapper<WechatAuth>()
                    .eq("openid", wechatAuth.getOpenid()));
            if (effectNum <= 0) {
                throw new ServiceException("bindWechatAuth failed");
            }
            return effectNum;
        } catch (Exception e) {
            log.error("bindWechatAuth", e);
            throw new ServiceException("bindWechatAuth failed:" + e.getMessage());
        }
    }

    @Override
    public int getCountByUserId(String userId) {
        return wechatAuthMapper.selectCount(new QueryWrapper<WechatAuth>()
                .eq("user_id", userId));
    }

    @Override
    public int cancelBind(String openId) throws ServiceException {
        try {
            WechatAuth wechatAuth = new WechatAuth();
            wechatAuth.setOpenid(openId);
            wechatAuth.setUserId(null);
            wechatAuth.setType(null);
            wechatAuth.setModifytime(new Date());
            int effectNum = wechatAuthMapper.cancelBind(openId);
            if (effectNum < 1) {
                throw new ServiceException("cancelBind sql failed");
            }
            return effectNum;
        } catch (Exception e) {
            log.error("cancelBind failed " + e);
            throw e;
        }
    }

    @Override
    public List<Shop> getSecretBindInfo(WechatAuth wechatAuth) throws ServiceException {
        try {
            /**
             * 1.根据微信用户id查询有效的秘钥绑定,获得店铺id
             * 2.根据店铺id获取店铺信息
             */
            List<OrderUserSecret> orderUserSecrets = orderUserSecretMapper.selectList(new QueryWrapper<OrderUserSecret>()
                    .select("id")
                    .eq("user_id", wechatAuth.getWechatAuthId())
                    .eq("dr", 0));
            //如果没有有效的绑定秘钥信息,则返回空
            if (orderUserSecrets.size() < 1 || null == orderUserSecrets) {
                return null;
            }
            List<Shop> shops = new ArrayList<>();
            Shop shop;
            OrderSecret orderSecret;
            for (OrderUserSecret orderUserSecret : orderUserSecrets) {
                orderSecret = orderSecretMapper.selectOne(new QueryWrapper<OrderSecret>()
                        .select("shop_id", "company_id")
                        .eq("id", orderUserSecret.getSecretId())
                        .eq("dr", 0));
                shop = shopMapper.selectOne(new QueryWrapper<Shop>()
                        .eq("id", orderSecret.getShopid())
                        .eq("company_id", orderSecret.getCompanyid())
                        .eq("dr", 0));
                shops.add(shop);
            }
            return shops;
        } catch (Exception e) {
            log.error("getSecretBindInfo error:" + e);
            throw new ServiceException("getSecretBindInfo error:" + e);
        }
    }

    @Override
    public Message<WechatAuth> registerOrderUser(WechatAuth wechatAuth) throws ServiceException {
        Message<WechatAuth> message = null;
        //空值判断
        if (wechatAuth == null || wechatAuth.getOpenid() == null) {
            message = new Message();
            message.setState(WechatAuthStateEnum.LOGINFAIL.getState());
            message.setMessage(WechatAuthStateEnum.NULL_AUTH_INFO.getStateInfo());
            return message;
        }
        //如果微信账号里夹带着用户信息并且用户id为空,则认为该用户第一次使用平台,且使用微信登录
        //自动创建用户信息
        try {
            wechatAuth.setCreatetime(new Date());
            //创建专属于本平台的微信账号
            int effectedNum = wechatAuthMapper.insert(wechatAuth);
            if (effectedNum <= 0) {
                message = new Message();
                message.setState(WechatAuthStateEnum.REGISTERFAIL.getState());
                message.setMessage(WechatAuthStateEnum.REGISTERFAIL.getStateInfo());
                return message;
            } else {
                message = new Message();
                message.setData(wechatAuth);
                message.setState(WechatAuthStateEnum.SUCCESS.getState());
                message.setMessage(WechatAuthStateEnum.SUCCESS.getStateInfo());
                return message;
            }
        } catch (Exception e) {
            log.error("insertWechatAuth error:" + e.toString());
            throw new RuntimeException("insertWechatAuth error: "
                    + e.getMessage());
        }
    }
}
