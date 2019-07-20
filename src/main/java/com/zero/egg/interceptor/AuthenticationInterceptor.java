package com.zero.egg.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.SassUser;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IUserService;
import com.zero.egg.service.SassUserService;
import com.zero.egg.tool.AuthenticateException;
import com.zero.egg.tool.TokenUtils;
import com.zero.egg.tool.UtilConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;

    @Autowired
    private ICompanyUserService companyUserService;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private SassUserService sassUserService;


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
            String redisKey = request.getHeader("token");

            if (null == redisKey) {
                log.debug("redis中对应token的键为null");
                throw new AuthenticateException(401, "无token，请重新登录");
            } else {
                Claims claims;
                try {
                    // 从Redis 中查看 token 是否过期
                    String accessToken = jedisStrings.get(UtilConstants.RedisPrefix.USER_REDIS + redisKey);
                    if (null == accessToken || "".equals(accessToken)) {
                        log.debug("redis中token对应的值为null");
                        throw new AuthenticateException(401, "token失效，请重新登录");
                    }
                    claims = TokenUtils.parseJWT(accessToken);
                } catch (ExpiredJwtException eje) {
                    response.setStatus(401);
                    log.error("ExpiredJwtException:" + eje);
                    throw new AuthenticateException(401, "token失效，请重新登录");
                } catch (SignatureException se) {
                    log.error("SignatureException:" + se);
                    response.setStatus(401);
                    throw new AuthenticateException(401, "token令牌错误");
                } catch (Exception e) {
                    log.error("Authentication Failed:" + e);
                    throw new AuthenticateException(401, "token失效，请重新登录");
                }

                String userId = claims.getId();
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", userId)
                        .eq("dr", false)
                        .eq("status", UserEnums.Status.Normal.index().toString());
                User user = userService.getOne(userQueryWrapper);
                LoginUser loginUser = new LoginUser();
                /**
                 * 1.店铺账号是否能查到,查得到就返回相应信息
                 * 2.如果不是店铺账号,查是否是企业账号
                 * 3.如果不是企业账号就查是否是Sass平台账号(后期服务拆分时拆分)
                 */
                if (user == null) {
                    QueryWrapper<CompanyUser> cUserQueryWrapper = new QueryWrapper<>();
                    cUserQueryWrapper.eq("id", userId)
                            .eq("dr", false)
                            .eq("status", CompanyUserEnums.Status.Normal.index().toString());
                    CompanyUser companyUser = companyUserService.getOne(cUserQueryWrapper);
                    if (companyUser == null) {
                        SassUser sassUser = sassUserService.selectByPrimaryKey(userId);
                        if (null == sassUser) {
                            response.setStatus(401);
                            throw new AuthenticateException(401, "用户不存在，请重新登录");
                        } else {
                            // 当前登录用户@CurrentUser
                            loginUser.setId(sassUser.getId());
                            loginUser.setLoginname(sassUser.getLoginname());
                            request.setAttribute(ApiConstants.LOGIN_USER, loginUser);
                        }
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
                    //如果当前用户是移动端,则需验证微信登录是否过期
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
