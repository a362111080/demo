package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.zero.egg.model.OrderAd")
@Data
@TableName(value = "order_ad")
public class OrderAd implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 广告标题
     */
    @TableField(value = "name")
    @ApiModelProperty(value="广告标题")
    private String name;

    /**
     * 所广告的商品页面或者活动页面链接地址
     */
    @TableField(value = "link")
    @ApiModelProperty(value="所广告的商品页面或者活动页面链接地址")
    private String link;

    /**
     * 广告宣传图片
     */
    @TableField(value = "url")
    @ApiModelProperty(value="广告宣传图片")
    private String url;

    /**
     * 广告位置：1则是首页
     */
    @TableField(value = "position")
    @ApiModelProperty(value="广告位置：1则是首页")
    private Byte position;

    /**
     * 活动内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value="活动内容")
    private String content;

    /**
     * 广告开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value="广告开始时间")
    private Date startTime;

    /**
     * 广告结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value="广告结束时间")
    private Date endTime;

    /**
     * 是否启动
     */
    @TableField(value = "enabled")
    @ApiModelProperty(value="是否启动")
    private Boolean enabled;

    /**
     * 创建人
     */
    @TableField(value = "creator")
    @ApiModelProperty(value="创建人")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    @ApiModelProperty(value="创建时间")
    private Date createtime;

    /**
     * 修改人
     */
    @TableField(value = "modifier")
    @ApiModelProperty(value="修改人")
    private String modifier;

    /**
     * 更新时间
     */
    @TableField(value = "modifytime")
    @ApiModelProperty(value="更新时间")
    private Date modifytime;

    /**
     * 逻辑删除
     */
    @TableField(value = "dr")
    @ApiModelProperty(value="逻辑删除")
    private Boolean dr;

    private static final long serialVersionUID = 1L;
}