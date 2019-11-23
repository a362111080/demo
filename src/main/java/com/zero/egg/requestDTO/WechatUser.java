package com.zero.egg.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信用户实体类，用来接收昵称 openid等用户信息
 *
 * @ClassName WechatUser
 * @Author lyming
 * @Date 2019/4/25 14:31
 **/
@Data
public class WechatUser implements Serializable {

    private static final long serialVersionUID = 4417451726498106896L;

    // openId,标识该公众号下面的该用户的唯一Id
    @JsonProperty("openid")
    private String openId;
    // 用户昵称
    @JsonProperty("nickname")
    private String nickName;
    // 性别
    @JsonProperty("sex")
    private int sex;
    // 省份
    @JsonProperty("province")
    private String province;
    // 城市
    @JsonProperty("city")
    private String city;
    // 区
    @JsonProperty("country")
    private String country;
    // 头像图片地址
    @JsonProperty("headimgurl")
    private String headimgurl;
    // 语言
    @JsonProperty("language")
    private String language;
    // 用户权限
    @JsonProperty("privilege")
    private String[] privilege;
}
