package com.zero.egg.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.egg.tool.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MyAuthenticationEntryPointHandler
 * @Description 认证失败处理类
 * 在访问一个受保护的资源，用户没有通过登录认证
 * @Author lyming
 * @Date 2019-08-03 00:16
 **/
@Component
public class MyAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        StringBuffer msg = new StringBuffer("请求访问: ");
        msg.append(httpServletRequest.getRequestURI()).append(" 接口，因为认证失败，无法访问系统资源.");
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new Message(401,msg.toString())));
    }
}
