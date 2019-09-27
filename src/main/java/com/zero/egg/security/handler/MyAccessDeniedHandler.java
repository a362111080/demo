package com.zero.egg.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.egg.tool.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MyAccessDeniedHandler
 * @Description 当用户登录系统后访问资源时因权限不足则会进入到此类并执行相关业务
 * @Author lyming
 * @Date 2019-08-02 23:28
 **/
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        StringBuffer msg = new StringBuffer("请求: ");
        msg.append(request.getRequestURI()).append(" 权限不足，无法访问该资源.");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new Message(401,msg.toString())));
    }
}