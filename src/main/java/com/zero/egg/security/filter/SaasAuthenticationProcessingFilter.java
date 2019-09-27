package com.zero.egg.security.filter;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName SaasAuthenticationProcessingFilter
 * @Author lyming
 * @Date 2019/9/26 6:36 上午
 **/
public class SaasAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public SaasAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/system/saaslogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = request.getParameter("loginname");
        String password = request.getParameter("loginPwd");
        if (username == null) {
            throw new InternalAuthenticationServiceException("Failed to get the username");
        }

        if (password == null) {
            throw new InternalAuthenticationServiceException("Failed to get the password");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
