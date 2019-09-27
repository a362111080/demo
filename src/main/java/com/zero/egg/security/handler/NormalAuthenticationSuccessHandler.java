package com.zero.egg.security.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BdCityMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.BdCity;
import com.zero.egg.model.Shop;
import com.zero.egg.security.user.SecurityUserDetails;
import com.zero.egg.tool.MD5Utils;
import com.zero.egg.tool.TokenUtils;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NormalAuthenticationSuccessHandler
 * @Author lyming
 * @Date 2019/9/26 9:07 上午
 **/
@Component
@Slf4j
public class NormalAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private BdCityMapper bdCityMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        BaseResponse<Object> message = new BaseResponse<>();
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        try {
            String redisKey = MD5Utils.encodeWithFixSalt(securityUserDetails.getLoginname() + securityUserDetails.getPassword());
            Map<String, Object> map = new HashMap<>();
            int type = securityUserDetails.getType();
            String accessToken = TokenUtils.createJwtToken(securityUserDetails, type);
            //登录信息额外加入当前店铺地区信息
            Shop shop = shopMapper.selectOne(new QueryWrapper<Shop>().select("city_id").eq("id", securityUserDetails.getShopId()));
            StringBuffer sb = new StringBuffer();
            //查出cityId上两级
            String parent2 = bdCityMapper.selectOne(new QueryWrapper<BdCity>()
                    .select("parent_id")
                    .eq("id", shop.getCityId()))
                    .getParentId();
            String parent1 = bdCityMapper.selectOne(new QueryWrapper<BdCity>()
                    .select("parent_id")
                    .eq("id", parent2))
                    .getParentId();
            sb.append(parent1);
            sb.append("," + parent2);
            sb.append("," + shop.getCityId());
            map.put("shopAddress", sb);
            map.put("token", redisKey);
            map.put("userType", type);
            map.put("userTypeName", UserEnums.Type.note(type));
            map.put("user", securityUserDetails);
            jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
            message.setData(map);
            message.setCode(ApiConstants.ResponseCode.SUCCESS);
            message.setMsg("登录成功");
        } catch (Exception e) {
            log.error("生成redisKey失败" + e);
            message.setCode(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMsg(UtilConstants.ResponseMsg.FAILED);
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
