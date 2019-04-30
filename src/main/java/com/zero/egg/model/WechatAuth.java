package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
     * 微信openid
     */
    private String openid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 最后一次绑定时间
     */
    @ApiModelProperty(value = "最后一次绑定时间", required = false)
    private Date modifytime;

    /**
     * 与用户实体类关联
     */
    private String userId;

    /**
     * 用户类型(1.企业员工 2.店铺用户)
     */
    private Integer type;
}
