package com.zero.egg.interceptor;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @author jinbin
 * @date 2018-07-08 20:41
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;
    @Autowired
    private ICompanyUserService companyUserService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 判断接口是否需要登录
        LoginToken methodAnnotation = method.getAnnotation(LoginToken.class);

        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
            // 判断是否存在令牌信息，如果存在，则允许登录
            String accessToken = request.getHeader("token");


            if (null == accessToken) {
                throw new ServiceException("401", "无token，请重新登录");
            } else {
                // 从Redis 中查看 token 是否过期
                Claims claims;
                try {
                    claims = TokenUtils.parseJWT(accessToken);
                } catch (ExpiredJwtException e) {
                    response.setStatus(401);
                    throw new ServiceException("401", "token失效，请重新登录");
                } catch (SignatureException se) {
                    response.setStatus(401);
                    throw new ServiceException("401", "token令牌错误");
                }

                String userId = claims.getId();
                User user = userService.getById(userId);
                LoginUser loginUser = new LoginUser();
                if (user == null) {
                    CompanyUser companyUser = companyUserService.getById(userId);
                    if (companyUser == null) {
                        response.setStatus(401);
                        throw new ServiceException("401", "用户不存在，请重新登录");
                    } else {
                        // 当前登录用户@CurrentUser
                        loginUser.setId(companyUser.getId());
                        loginUser.setCode(companyUser.getCode());
                        loginUser.setName(companyUser.getName());
                        loginUser.setLoginname(companyUser.getLoginname());
                        loginUser.setPhone(companyUser.getPhone());
                        loginUser.setCompanyId(companyUser.getCompanyId());
                        request.setAttribute(ApiConstants.LOGIN_USER, loginUser);
                        request.setAttribute(ApiConstants.LOGIN_TYPE, 1);
                        request.setAttribute(ApiConstants.USER_TYPE, UserEnums.Type.Company.index());
                    }
                } else {
                    // 当前登录用户@CurrentUser
                    loginUser.setId(user.getId());
                    loginUser.setCode(user.getCode());
                    loginUser.setName(user.getName());
                    loginUser.setLoginname(user.getLoginname());
                    loginUser.setPhone(user.getPhone());
                    loginUser.setCompanyId(user.getCompanyId());
                    loginUser.setShopId(user.getShopId());
                    request.setAttribute(ApiConstants.LOGIN_USER, loginUser);
                    request.setAttribute(ApiConstants.LOGIN_TYPE, 2);
                    request.setAttribute(ApiConstants.USER_TYPE, user.getType());
                }
                return true;
            }

        } else {//不需要登录可请求
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
