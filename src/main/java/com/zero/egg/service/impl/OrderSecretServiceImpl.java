package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.OrderSecretMapper;
import com.zero.egg.dao.OrderUserSecretMapper;
import com.zero.egg.model.OrderSecret;
import com.zero.egg.model.OrderUserSecret;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.OrderSecretService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Message bindSecret(String secret, LoginUser user) throws ServiceException {
        /**
         * 1.根据secret查询order_secret获取id
         * 2.根据id查询是否已经被绑定(secret已建立唯一索引)
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
            String secretId = orderSecret.getId();
            OrderUserSecret orderUserSecret = orderUserSecretMapper
                    .selectOne(new QueryWrapper<OrderUserSecret>()
                            .eq("secret_id", secretId)
                            .eq("dr", false));
            if (null == orderUserSecret || null != orderUserSecret.getUserId()) {
                log.error("=======secret has been used=======");
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SECRET_HAS_BEEN_USED);
                return message;
            }
            //绑定
            orderUserSecret.setUserId(user.getId());
            orderUserSecretMapper.update(orderUserSecret, new UpdateWrapper<OrderUserSecret>()
                    .eq("id", orderUserSecret.getId()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;

        } catch (Exception e) {
            log.error("bindSecret service error:" + e);
            throw new ServiceException("bindSecret service error:" + e);
        }
    }
}
