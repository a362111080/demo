package com.zero.egg.security.provider;

import com.zero.egg.security.service.CompanyUserDetailsServiceImpl;
import com.zero.egg.tool.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @ClassName CompanyUserDaoAuthenticationProvider
 * @Author lyming
 * @Date 2019/9/27 2:35 下午
 **/
@Component
public class CompanyUserDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private CompanyUserDetailsServiceImpl companyUserDetailsService;

    @Autowired
    private MD5Utils md5Utils;
    /**
     * 校验密码有效性.
     * @param userDetails
     * @param usernamePasswordAuthenticationToken
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        if (usernamePasswordAuthenticationToken.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = usernamePasswordAuthenticationToken.getCredentials().toString();

        if (!md5Utils.matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }


    /**
     * 获取用户
     * @param username
     * @param usernamePasswordAuthenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        UserDetails loadedUser = companyUserDetailsService.loadUserByUsername(username);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    /**
     * 授权持久化
     * @param principal
     * @param authentication
     * @param user
     * @return
     */
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return super.createSuccessAuthentication(principal, authentication, user);
    }
}
