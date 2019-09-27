package com.zero.egg.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName WxbindAuthenticationProcessingFilter
 * @Author lyming
 * @Date 2019/9/26 6:36 上午
 **/
public class WxbindAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public WxbindAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/wxLogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
