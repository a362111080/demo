package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.OrderSecretMapper;
import com.zero.egg.dao.OrderUserSecretMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.dao.WechatAuthMapper;
import com.zero.egg.model.OrderSecret;
import com.zero.egg.model.OrderUserSecret;
import com.zero.egg.model.Shop;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.OrderSecretService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderSecretServiceImpl
 * @Author lyming
 * @Date 2019/8/27 3:54 下午
 **/
@Slf4j
@Service
public class OrderSecretServiceImpl implements OrderSecretService {

    @Autowired
    private OrderUserSecretMapper orderUserSecretMapper;

    @Autowired
    private OrderSecretMapper orderSecretMapper;

    @Autowired
    private WechatAuthMapper wechatAuthMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    @Transactional
    public Message bindSecret(String secret, LoginUser user) throws ServiceException {
        /**
         * pre:查询改用户是否在对应店铺下有绑定关系
         * 1.根据secret查询order_secret获取id(secret已建立唯一索引)
         * 2.根据id查询是否已经被绑定
         * 3.绑定
         */
        Message message = new Message();
        try {
            OrderSecret orderSecret = orderSecretMapper.selectOne(new QueryWrapper<OrderSecret>()
                    .eq("secret_key", secret)
                    .eq("dr", false));
            if (null == orderSecret || null == orderSecret.getId()) {
                log.error("=======no such secret=======");
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_SUCH_SECRET);
                return message;
            }
            String shopId = orderSecret.getShopid();
            Integer bindCount = orderSecretMapper.getCountOfShopBind(shopId, user.getId());
            if (bindCount != null && bindCount > 0) {
                log.error("=======用户已经绑定该店铺,不用重复绑定=======");
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SHOP_BINDED);
                return message;
            }
            if (orderSecret.getStatus()) {
                log.error("=======secret has been used=======");
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SECRET_HAS_BEEN_USED);
                return message;
            }
            String secretId = orderSecret.getId();
            OrderUserSecret orderUserSecret = orderUserSecretMapper
                    .selectOne(new QueryWrapper<OrderUserSecret>()
                            .eq("secret_id", secretId)
                            .eq("dr", false));
            if (null != orderUserSecret && null != orderUserSecret.getUserId()) {
                log.error("=======secret has been used=======");
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SECRET_HAS_BEEN_USED);
                return message;
            }
            //绑定
            if (null != orderUserSecret) {
                orderUserSecret.setUserId(user.getId());
                orderUserSecretMapper.update(orderUserSecret, new UpdateWrapper<OrderUserSecret>()
                        .eq("id", orderUserSecret.getId()));
            } else {
                orderUserSecret = new OrderUserSecret();
                orderUserSecret.setUserId(user.getId());
                orderUserSecret.setSecretId(secretId);
                orderUserSecret.setCreator(user.getId());
                orderUserSecret.setCreatetime(new Date());
                //中间表关联
                orderUserSecretMapper.insert(orderUserSecret);
                //秘钥表修改秘钥状态
                orderSecret.setStatus(true);
                orderSecretMapper.updateById(orderSecret);
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;

        } catch (Exception e) {
            log.error("bindSecret service error:" + e);
            throw new ServiceException("bindSecret service error:" + e);
        }
    }

    @Override
    public Message getShopList(LoginUser user) throws ServiceException {
        Message message = new Message();
        try {
            WechatAuth wechatAuth = wechatAuthMapper.selectById(user.getId());
            List<OrderUserSecret> orderUserSecrets = orderUserSecretMapper.selectList(new QueryWrapper<OrderUserSecret>()
                    .eq("user_id", wechatAuth.getWechatAuthId())
                    .eq("dr", 0));
            //如果没有有效的绑定秘钥信息,则返回空
            if (orderUserSecrets.size() < 1 || null == orderUserSecrets) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_COOPERATE_SHOP);
                return message;
            }
            List<Shop> shops = new ArrayList<>();
            Shop shop;
            OrderSecret orderSecret;
            for (OrderUserSecret orderUserSecret : orderUserSecrets) {
                orderSecret = orderSecretMapper.selectOne(new QueryWrapper<OrderSecret>()
                        .select("shop_id", "company_id", "secret_key")
                        .eq("id", orderUserSecret.getSecretId())
                        .eq("dr", 0)
                        .eq("status", 1));
                if (null == orderSecret) {
                    continue;
                }
                shop = shopMapper.selectOne(new QueryWrapper<Shop>()
                        .eq("id", orderSecret.getShopid())
                        .eq("company_id", orderSecret.getCompanyid())
                        .eq("dr", 0));
                shop.setSecret(orderSecret.getSecretKey());
                shops.add(shop);
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(shops);
            return message;
        } catch (Exception e) {
            log.error("getShopList service error:" + e);
            throw new ServiceException("getShopList service error:" + e);
        }
    }

    @Override
    @Transactional
    public Message cancelSecret(String secret, LoginUser user) throws ServiceException {
        /**
         * 1.根据秘钥查出秘钥信息,并逻辑删除(secret已建立唯一索引)
         * 2.根据秘钥id和userId查出关联表信息并删除
         */
        Message message = new Message();
        try {
            OrderSecret orderSecret = orderSecretMapper.selectOne(new QueryWrapper<OrderSecret>()
                    .eq("secret_key", secret)
                    .eq("dr", false));
            if (null == orderSecret || null == orderSecret.getId()) {
                log.error("=======no such secret=======");
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_SUCH_SECRET);
                return message;
            }
            //改变status状态
//            orderSecret.setDr(true);
            orderSecret.setStatus(false);
            //删除秘钥信息
            orderSecretMapper.updateById(orderSecret);
            orderUserSecretMapper.update(new OrderUserSecret().setDr(true), new UpdateWrapper<OrderUserSecret>()
                    .eq("secret_id", orderSecret.getId())
                    .eq("dr", false)
                    .eq("user_id", user.getId()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("cancelSecret service error:" + e);
            throw new ServiceException("cancelSecret service error:" + e);
        }
    }
}
