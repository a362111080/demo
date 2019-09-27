package com.zero.egg.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.AESUtil;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MyLogoutSuccessHandler
 * @Description 用户退出系统成功后, 需要做的业务操作
 * 包括：清空redis中的Token，清空上下文&Session，记录退出日志等等
 * @Author lyming
 * @Date 2019-08-03 00:19
 **/
@Component
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private JedisUtil.Keys jediskeys;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WechatAuthService wechatAuthService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //清空上下文
        SecurityContextHolder.clearContext();
        // 从session中移除
        httpServletRequest.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
        Message message = new Message();
        try {
            /**
             * 拦截器里已经获取过一遍,这里一定能获取到,否则不会进方法
             */
            String redisKey = httpServletRequest.getHeader("token");
            /** 清除redis里的token信息 */
            jediskeys.del(UtilConstants.RedisPrefix.USER_REDIS + redisKey);
            //如果header里面有request.getHeader("wxSessionkey"),则解除绑定
            if (null != httpServletRequest.getHeader("wxSessionkey") || !"".equals(httpServletRequest.getHeader("wxSessionkey"))) {
                String openid = AESUtil.decrypt(httpServletRequest.getHeader("wxSessionkey"), AESUtil.KEY);
                wechatAuthService.cancelBind(openid);
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("logout exception:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(message));

    }
}
