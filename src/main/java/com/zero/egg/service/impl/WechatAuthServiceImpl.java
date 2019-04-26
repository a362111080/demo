package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.UserMapper;
import com.zero.egg.dao.WechatAuthMapper;
import com.zero.egg.enums.WechatAuthStateEnum;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    private UserMapper userMapper;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthMapper.selectOne(new QueryWrapper<WechatAuth>()
                .eq("open_id", openId));
    }

    @Override
    @Transactional
    public Message register(WechatAuth wechatAuth) throws ServiceException {
        Message message = null;
        //空值判断
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            message = new Message();
            message.setState(WechatAuthStateEnum.LOGINFAIL.getState());
            message.setMessage(WechatAuthStateEnum.NULL_AUTH_INFO.getStateInfo());
            return message;
        }
        //如果微信账号里夹带着用户信息并且用户id为空,则认为该用户第一次使用平台,且使用微信登录
        //自动创建用户信息
        try {
            wechatAuth.setCreateTime(new Date());
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
                .eq("open_id", openId));
    }
}
