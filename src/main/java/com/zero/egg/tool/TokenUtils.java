package com.zero.egg.tool;

import com.alibaba.fastjson.JSON;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.security.user.SecurityUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class TokenUtils {
    /**
     * JWT签名加密key
     */
    public static String secret;
    /**
     * token参数头
     */
    public static String tokenHeader;
    /**
     * 权限参数头
     */
    public static String authHeader;
    /**
     * 用户类型参数头
     */
    public static String typeHeader;
    /**
     * token有效期
     */
    public static Integer tokenExpireTime;

    /**
     * 不需要认证的接口
     */
    public static String antMatchers;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        TokenUtils.secret = secret;
    }

    @Value("${jwt.tokenHeader}")
    public void setTokenHeader(String tokenHeader) {
        TokenUtils.tokenHeader = tokenHeader;
    }

    @Value("${jwt.authHeader}")
    public void setAuthHeader(String authHeader) {
        TokenUtils.authHeader = authHeader;
    }

    @Value("${jwt.expiration}")
    public void setTokenExpireTime(Integer tokenExpireTime) {
        TokenUtils.tokenExpireTime = tokenExpireTime;
    }

    @Value("${jwt.antMatchers}")
    public void setAntMatchers(String antMatchers) {
        TokenUtils.antMatchers = antMatchers;
    }

    @Value("${jwt.typeHeader}")
    public void setTypeHeader(String typeHeader) {
        TokenUtils.typeHeader = typeHeader;
    }


    /**
     * 生成Token
     *
     * @return token String
     */
    public static String createJwtToken(UserDetails userDetails, Integer type) {
        return createJwtToken(userDetails, "", type);
    }

    public static String createJwtToken(UserDetails userDetails, String issuer, Integer type) {
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) userDetails;

        // 签名算法 ，将对token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        String subject = null;
        if (type.equals(UserEnums.Type.Order.index())) {
            //如果用户类型为订货平台客户(没有绑定本地用户),则subject为""
            subject = "";
        }else{
            subject = securityUserDetails.getLoginname();
        }
        JwtBuilder builder = Jwts.builder()
                .setId(securityUserDetails.getId() + "")
                .setSubject(subject)
                .setIssuer(issuer)
                //自定义属性 放入用户拥有权限
                .claim(authHeader, JSON.toJSONString(securityUserDetails.getAuthorities()))
                //自定义属性 放入用户类型
                .claim(typeHeader, type)
                //失效时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 24 * 60 * 60 * 1000))
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public String getUserIdFromToken(String token) {
        String userId;
        try {
            final Claims claims = parseJWT(token);
            userId = claims.getId();
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }


}
