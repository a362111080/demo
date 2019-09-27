package com.zero.egg.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.dao.WechatAuthMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.Company;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.SaasUser;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IUserService;
import com.zero.egg.service.SaasUserService;
import com.zero.egg.service.impl.CompanyServiceImpl;
import com.zero.egg.service.impl.CompanyUserServiceImpl;
import com.zero.egg.service.impl.SaasUserServiceImpl;
import com.zero.egg.service.impl.UserServiceImpl;
import com.zero.egg.tool.AuthenticateException;
import com.zero.egg.tool.SpringUtils;
import com.zero.egg.tool.TokenUtils;
import com.zero.egg.tool.UtilConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JWTAuthenticationFilter
 * @Description jwt认证token
 * 每次请求接口时，就会进入这里验证token是否合法token，
 * 如果用户一直在操作，则token 过期时间会叠加；如果超过设置的过期时间未操作，则token 失效，需要重新登录。
 * @Author lyming
 * @Date 2019-08-03 00:12
 **/
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {


    @Autowired
    private IUserService userService;

    @Autowired
    private ICompanyUserService companyUserService;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private SaasUserService saasUserService;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private WechatAuthMapper wechatAuthMapper;

    @Autowired
    private ICompanyService iCompanyService;


//    @Override
//    protected void initFilterBean() throws ServletException {
//        if (jedisStrings == null) {
//            jedisStrings = (JedisUtil.Strings) SpringUtils.getBean("jedisStrings");
//        }
//        if (jediskeys == null) {
//            jediskeys = (JedisUtil.Keys) SpringUtils.getBean("jediskeys");
//        }
//    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //过滤掉不需要token验证的url
        /*if(authenticationRequestMatcher != null && !authenticationRequestMatcher.matches(httpServletRequest)){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }*/
        //获得redisKey
        String redisKey = request.getHeader("token");
        if (null != redisKey ) {
            try {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
        return;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String redisKey = request.getHeader("token");
        if (!StringUtils.isEmpty(redisKey)) {
            try {
                jedisStrings = (JedisUtil.Strings) SpringUtils.getBean("jedisStrings");
                jedisKeys = (JedisUtil.Keys) SpringUtils.getBean("jedisKeys");
                userService =  SpringUtils.getBean(UserServiceImpl.class);
                companyUserService =  SpringUtils.getBean(CompanyUserServiceImpl.class);
                shopMapper =  SpringUtils.getBean(ShopMapper.class);
                wechatAuthMapper = SpringUtils.getBean(WechatAuthMapper.class);
                iCompanyService =  SpringUtils.getBean(CompanyServiceImpl.class);
                saasUserService = SpringUtils.getBean(SaasUserServiceImpl.class);
                // 从Redis 中查看 token 是否过期
                String accessToken = jedisStrings.get(UtilConstants.RedisPrefix.USER_REDIS + redisKey);
                if (StringUtils.isEmpty(accessToken)) {
                    log.debug("redis中token对应的值为null");
                    throw new AuthenticateException(401, "token has expired");
                }
                Claims claims = TokenUtils.parseJWT(accessToken);
                //存在session中
                LoginUser loginUser = new LoginUser();
                //获取用户名
                String username = claims.getSubject();
                String userId = claims.getId();
                Integer type = (Integer) claims.get(TokenUtils.typeHeader);
                if (type == null) {
                    throw new AuthenticateException(401, "type has expired");
                }
                switch (type) {
                    case 0:
                        //企业端
                        QueryWrapper<CompanyUser> cUserQueryWrapper = new QueryWrapper<>();
                        cUserQueryWrapper.eq("id", userId)
                                .eq("dr", false)
                                .eq("status", CompanyUserEnums.Status.Normal.index().toString());
                        CompanyUser companyUser = companyUserService.getOne(cUserQueryWrapper);
                        if (null == companyUser) {
                            throw new AuthenticateException(401, "账号不存在");
                        }
                        QueryWrapper<Company> companyQueryWrapper1 = new QueryWrapper<>();
                        companyQueryWrapper1.eq("id", companyUser.getCompanyId()).eq("dr", false);
                        //验证企业是否过期
                        Company company1 = iCompanyService.getOne(companyQueryWrapper1);
                        if (null == company1) {
                            //删除redis中token信息
                            jedisKeys.del(UtilConstants.RedisPrefix.USER_REDIS + redisKey);
                            throw new AuthenticateException(401, "账号需要续费");
                        }
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
                        break;
                    case 1:
                        //PC客户端
                        QueryWrapper<com.zero.egg.model.User> userQueryWrapper = new QueryWrapper<>();
                        userQueryWrapper.eq("id", userId)
                                .eq("dr", false)
                                .eq("status", UserEnums.Status.Normal.index().toString());
                        com.zero.egg.model.User normalUser = userService.getOne(userQueryWrapper);
                        if (null == normalUser) {
                            throw new AuthenticateException(401, "账号不存在");
                        };
                        QueryWrapper<Company> companyQueryWrapper2 = new QueryWrapper<>();
                        companyQueryWrapper2.eq("id", normalUser.getCompanyId()).eq("dr", false);
                        //验证企业是否过期
                        Company company = iCompanyService.getOne(companyQueryWrapper2);
                        if (null == company) {
                            //删除redis中token信息
                            jedisKeys.del(UtilConstants.RedisPrefix.USER_REDIS + redisKey);
                            throw new AuthenticateException(401, "账号需要续费");
                        }
                        /**
                         * 对于PC用户,额外存储店铺类型
                         */
                        Shop shop = shopMapper.selectOne(new QueryWrapper<Shop>().select("type").eq("id", normalUser.getShopId()));
                        request.setAttribute(ApiConstants.SHOP_TYPE,shop.getType());
                        loginUser.setId(normalUser.getId());
                        loginUser.setCode(normalUser.getCode());
                        loginUser.setName(normalUser.getName());
                        loginUser.setLoginname(normalUser.getLoginname());
                        loginUser.setPhone(normalUser.getPhone());
                        loginUser.setCompanyId(normalUser.getCompanyId());
                        loginUser.setShopId(normalUser.getShopId());
                        request.setAttribute(ApiConstants.LOGIN_USER, loginUser);
                        request.setAttribute(ApiConstants.LOGIN_TYPE, 2);
                        request.setAttribute(ApiConstants.USER_TYPE, normalUser.getType());
                        break;
                    case 2:
                        //Boss移动端
                        break;
                    case 3:
                        //员工端
                        break;
                    case 4:
                        //设备端
                        break;
                    case 5:
                        //SAAS平台端
                        SaasUser saasUser = saasUserService.selectByPrimaryKey(userId);
                        loginUser.setId(saasUser.getId());
                        loginUser.setLoginname(saasUser.getLoginname());
                        request.setAttribute(ApiConstants.LOGIN_USER, loginUser);
                        break;
                    case 6:
                        //订货平台端
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + type);
                }

                //获取权限（角色）
                List<GrantedAuthority> authorities = new ArrayList<>();
                String authority = claims.get(TokenUtils.authHeader).toString();
                if (!StringUtils.isEmpty(authority)) {
                    //authority="[{"authority":"common"}]"
                    List<Map<String, String>> authrityMap = JSONObject.parseObject(authority, List.class);
                    for (Map<String, String> role : authrityMap) {
                        if (!StringUtils.isEmpty(role)) {
                            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                        }
                    }
                }
                if (!StringUtils.isEmpty(username)) {
                    //此处password不能为null
                    User principal = new User(username, "", authorities);
                    return new UsernamePasswordAuthenticationToken(principal, userId, authorities);
                }
            } catch (ExpiredJwtException e) {
                log.error("toekn超过有效期，请重新登");
                throw new AuthenticateException(401, "token has expired");
            } catch (Exception e) {
                throw new AuthenticateException(401, "token invalid");
            }
        }
        return null;
    }

}
