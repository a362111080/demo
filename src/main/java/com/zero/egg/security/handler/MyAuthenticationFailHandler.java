package com.zero.egg.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.egg.tool.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MyAuthenticationFailHandler
 * @Description 登录失败处理类
 * 用户登录系统失败后需要做的业务操作
 * 包括：分类返回登录失败原因
 * @Author lyming
 * @Date 2019-08-03 00:17
 **/
@Component
public class MyAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
/*        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            //可在此记录登录失败次数，进行锁定

        } else if (e instanceof DisabledException) {
            //可以新增登录异常次数超限LoginFailLimitException
        } else {
        }*/
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new Message(401,e.getMessage())));
    }
}
