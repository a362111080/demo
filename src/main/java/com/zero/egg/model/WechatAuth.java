package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName WechatAuth
 * @Author lyming
 * @Date 2019/4/25 15:38
 **/
@Data
@TableName(value = "bd_wechat_auth")
public class WechatAuth {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String wechatAuthId;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 与用户实体类关联
     */
    private User user;
}
