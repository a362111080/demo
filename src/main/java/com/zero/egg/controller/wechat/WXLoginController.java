package com.zero.egg.controller.wechat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.enums.WechatAuthStateEnum;
import com.zero.egg.model.Shop;
import com.zero.egg.model.User;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.requestDTO.WXSessionModel;
import com.zero.egg.service.IUserService;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.AESUtil;
import com.zero.egg.tool.HttpClientUtil;
import com.zero.egg.tool.JsonUtils;
import com.zero.egg.tool.MD5Utils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.TokenUtils;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
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
    private IUserService userService;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;



    @PostMapping(("/wxLogin"))
    @PassToken
    @ApiOperation(value = "微信登录授权")
    public Message wxLogin(String code) {
        Message message;
        WechatAuth wechatAuth;
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wxb13b535d9bc9ab0c");
            param.put("secret", "1e6b53234cbe5744dd61499f5d8f3064");
            param.put("js_code", code);
            param.put("grant_type", "authorization_code");

            String wxResult = HttpClientUtil.doGet(url, param);
            String openId = null;
            String wxSessionkey = null;
            String nickname = null;
            if (!"".equals(wxResult) && null != wxResult) {
                WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
                openId = model.getOpenid();
                nickname = model.getNickname();
                /**
                 * 对称加密openid和session_key信息
                 */
                wxSessionkey = AESUtil.encrypt(openId, AESUtil.KEY);
                String wxToken = AESUtil.encrypt(wxResult, AESUtil.KEY);
                // 存入session到redis
                jedisStrings.setEx(UtilConstants.RedisPrefix.WXUSER_REDIS_SESSION + wxSessionkey,
                        60 * 60 * 2,
                        wxToken);
            } else {
                message = new Message();
                message.setState(UtilConstants.WXState.FAIL);
                message.setMessage(UtilConstants.ResponseMsg.HTTPAPI_ERROR);
                return message;
            }
            //如果根据openid查询不到本地账号注册信息
            wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            Map<String, Object> map;
            //如果能查到微信信息且绑定了本地账号信息,则生成token,返回本地账号信息和token
            if (null != wechatAuth && null != wechatAuth.getUserId() && null != wechatAuth.getType()
                    && !"".equals(wechatAuth.getUserId()) && !"".equals(wechatAuth.getType())) {
                String userId = wechatAuth.getUserId();
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", userId).eq("dr", false);
                User user = userService.getOne(userQueryWrapper);
                //如果用户被删除,则解除绑定,并提示用户重新登录
                if (null == user) {
                    wechatAuthService.cancelBind(openId);
                    message = new Message();
                    message.setState(UtilConstants.WXState.FAIL);
                    message.setMessage(UtilConstants.ResponseMsg.HTTPAPI_ERROR);
                    return message;
                }
                int type = user.getType();
                //生成token
                String accessToken = TokenUtils.createJwtToken(user.getId(), wxSessionkey);
                String redisKey = MD5Utils.encodeWithFixSalt(user.getLoginname() + user.getPassword());
                jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
                map = new HashMap<>();
                map.put("token", redisKey);
                map.put("userType", type);
                map.put("userTypeName", UserEnums.Type.note(type));
                map.put("user", user);
                map.put("wxSessionkey", wxSessionkey);
                message = new Message();
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                message.setMap(map);
                return message;
            }
            if (null == wechatAuth) {
                //完全查不到本系统的微信账号才去注册,避免重复注册
                wechatAuth = new WechatAuth();
                wechatAuth.setOpenid(openId);
                wechatAuth.setNickname(nickname);
                message = wechatAuthService.register(wechatAuth);
                if (message.getState() == WechatAuthStateEnum.SUCCESS.getState()) {
                    map = new HashMap<String, Object>();
                    map.put("wxSessionkey", wxSessionkey);
                    message.setMap(map);
                    return message;
                } else {
                    //返回错误信息
                    return message;
                }
            }
            message = new Message();
            map = new HashMap<String, Object>();
            map.put("wxSessionkey", wxSessionkey);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setMap(map);
            return message;
        } catch (Exception e) {
            log.error("wechat login failed:", e);
            message = new Message();
            message.setState(UtilConstants.WXState.FAIL);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }

    }

    /**
     * 微信登录状态监测,需要在请求头带token(监测本地账号信息是否过期)和wxSessionkey(监测redis是否过期)
     *
     * @return
     */
    @ApiOperation(value = "微信登录状态监测")
    @LoginToken
    @PostMapping("/checkwxsession")
    public Message checkSessionKey(HttpServletRequest request) {
        Message message = new Message();
        String wxSessionkey = request.getHeader("wxSessionkey");
        try {
            /**
             * 如果redis里存在对应key且对应value不为null,则认为登录时间没有过期
             */
            if (jedisKeys.exists(UtilConstants.RedisPrefix.WXUSER_REDIS_SESSION + wxSessionkey)
                    && null != jedisStrings.get(UtilConstants.RedisPrefix.WXUSER_REDIS_SESSION + wxSessionkey)) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                return message;
            }
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SESSION_KEY_TIMEOUT);
            return message;
        } catch (Exception e) {
            log.error("check wxsession failed:", e.toString());
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }

    @PostMapping(("/wxOrderLogin"))
    @PassToken
    @ApiOperation(value = "订货平台微信登录授权")
    public Message wxOrderLogin(String code) {
        Message<WechatAuth> message;
        WechatAuth wechatAuth;
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx5a6262472a05c6cb");
            param.put("secret", "2faca696f27f8bd34c912195238e5925");
            param.put("js_code", code);
            param.put("grant_type", "authorization_code");

            String wxResult = HttpClientUtil.doGet(url, param);
            String openId = null;
            String wxSessionkey = null;
            String nickname = null;
            if (!"".equals(wxResult) && null != wxResult) {
                WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
                openId = model.getOpenid();
                nickname = model.getNickname();
                /**
                 * 对称加密openid和session_key信息
                 */
                wxSessionkey = AESUtil.encrypt(openId, AESUtil.KEY);
                String wxToken = AESUtil.encrypt(wxResult, AESUtil.KEY);
                // 存入session到redis
                jedisStrings.setEx(UtilConstants.RedisPrefix.WXUSER_REDIS_SESSION + wxSessionkey,
                        60 * 60 * 2,
                        wxToken);
            } else {
                message = new Message();
                message.setState(UtilConstants.WXState.FAIL);
                message.setMessage(UtilConstants.ResponseMsg.HTTPAPI_ERROR);
                return message;
            }
            //根据openid查询是否注册
            wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            Map<String, Object> map;
            //如果能查到微信信息
            if (null != wechatAuth ) {
                //如果根据wechat_auth_id查询秘钥绑定信息
                List<Shop> shopList = wechatAuthService.getSecretBindInfo(wechatAuth);
                int type = wechatAuth.getType();
                //生成token
                String accessToken = TokenUtils.createJwtToken(wechatAuth.getWechatAuthId(), wxSessionkey);
                String redisKey = MD5Utils.encodeWithFixSalt(wechatAuth.getWechatAuthId() + wechatAuth.getType());
                jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
                map = new HashMap<>();
                map.put("token", redisKey);
                map.put("userType", type);
                map.put("userTypeName", UserEnums.Type.note(type));
                map.put("shopList", shopList);
                map.put("wxSessionkey", wxSessionkey);
                message = new Message();
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                message.setMap(map);
                return message;
            }else {
                //完全查不到本系统的微信账号才去注册,避免重复注册
                wechatAuth = new WechatAuth();
                wechatAuth.setOpenid(openId);
                wechatAuth.setNickname(nickname);
                wechatAuth.setType(UserEnums.Type.Order.index());
                message = wechatAuthService.registerOrderUser(wechatAuth);
                if (message.getState() == WechatAuthStateEnum.SUCCESS.getState()) {
                    //生成token
                    String redisKey = MD5Utils.encodeWithFixSalt(wechatAuth.getWechatAuthId() + wechatAuth.getType());
                    String accessToken = TokenUtils.createJwtToken(message.getData().getWechatAuthId(), wxSessionkey);
                    jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
                    map = new HashMap<String, Object>();
                    map.put("token", redisKey);
                    map.put("userType", UserEnums.Type.Order.index());
                    map.put("userTypeName", UserEnums.Type.Order.note());
                    map.put("shopList", null);
                    map.put("wxSessionkey", wxSessionkey);
                    message.setMap(map);
                    return message;
                } else {
                    //返回错误信息
                    return message;
                }
            }
        } catch (Exception e) {
            log.error("wechat login failed:", e);
            message = new Message();
            message.setState(UtilConstants.WXState.FAIL);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }

    }
}
