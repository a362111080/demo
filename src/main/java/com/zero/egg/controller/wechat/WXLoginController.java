package com.zero.egg.controller.wechat;

import com.zero.egg.annotation.PassToken;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.enums.WechatAuthStateEnum;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.requestDTO.WXSessionModel;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.HttpClientUtil;
import com.zero.egg.tool.JsonUtils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信登录(未优化)
 *
 * @ClassName WXLoginController
 * @Author lyming
 * @Date 2019/4/26 17:48
 **/
@RestController
@Slf4j
@Api(value = "微信")
public class WXLoginController {


    @Autowired
    private WechatAuthService wechatAuthService;

    @Autowired
    private JedisUtil.Strings jedisStrings;


    @PostMapping(("/wxLogin"))
    @PassToken
    @ApiOperation(value = "微信登录授权")
    public Message wxLogin(String code) {
        Message message;
        WechatAuth wechatAuth = new WechatAuth();
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wxb13b535d9bc9ab0c");
            param.put("secret", "1e6b53234cbe5744dd61499f5d8f3064");
            param.put("js_code", code);
            param.put("grant_type", "authorization_code");

            String wxResult = HttpClientUtil.doGet(url, param);
            String openId = null;
            String session_key = null;
            if (!"".equals(wxResult) && null != wxResult) {
                WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
                // 存入session到redis
                jedisStrings.setEx("user-redis-session:" + model.getOpenid(),
                        1000 * 60 * 30,
                        model.getSession_key());
                openId = new String(model.getOpenid());
                session_key = new String(model.getSession_key());
            } else {
                message = new Message();
                message.setState(UtilConstants.WXState.FAIL);
                message.setMessage(UtilConstants.ResponseMsg.HTTPAPI_ERROR);
                return message;
            }
            //同时存数据库,为绑定本地账号做准备
            wechatAuth.setOpenId(openId);
            //如果根据openid查询不到本地账号注册信息
            //注册本系统的微信账号
            int count = wechatAuthService.getCountByOpenId(openId);
            if (count <= 0) {
                message = wechatAuthService.register(wechatAuth);
                if (message.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                    return message;
                }
            }
            message = new Message();
            message.setState(UtilConstants.WXState.OK);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(session_key);
            return message;
        } catch (Exception e) {
            log.error("wechat login failed:", e.getMessage());
            message = new Message();
            message.setState(UtilConstants.WXState.FAIL);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }

    }

}
