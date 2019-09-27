package com.zero.egg.security.provider;

import com.zero.egg.security.service.NomalUserDetailsServiceImpl;
import com.zero.egg.tool.AuthenticateException;
import com.zero.egg.tool.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @ClassName NormDaoAuthenticationProvider
 * @Author lyming
 * @Date 2019/9/26 7:58 上午
 **/
@Component
public class NormDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private  NomalUserDetailsServiceImpl nomalUserDetailsService;

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

            throw new AuthenticateException("密码为空");
        }

        String presentedPassword = usernamePasswordAuthenticationToken.getCredentials().toString();

        if (!md5Utils.matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new AuthenticateException("密码错误");
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
        UserDetails loadedUser = nomalUserDetailsService.loadUserByUsername(username);
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
