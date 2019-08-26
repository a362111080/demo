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

/**
 * @author lym
 */
@ApiModel(value="订货平台收货地址")
@TableName(value = "order_address")
@Data
public class OrderAddress implements Serializable {

    private static final long serialVersionUID = -2812595224464379860L;

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 收货人名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value="收货人名称")
    private String name;

    /**
     * 订货平台用户表的用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="订货平台用户表的用户ID")
    private String userId;

    /**
     * 行政区域表的市ID
     */
    @TableField(value = "city_id")
    @ApiModelProperty(value="行政区域表的市ID")
    private String cityId;

    /**
     * 详细收货地址
     */
    @TableField(value = "address_detail")
    @ApiModelProperty(value="详细收货地址")
    private String addressDetail;

    /**
     * 地区编码
     */
    @TableField(value = "area_code")
    @ApiModelProperty(value="地区编码")
    private String areaCode;

    /**
     * 手机号码
     */
    @TableField(value = "tel")
    @ApiModelProperty(value="手机号码")
    private String tel;

    /**
     * 是否默认地址
     */
    @TableField(value = "is_default")
    @ApiModelProperty(value="是否默认地址")
    private Boolean isDefault = false;

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
    private Boolean dr = false;

 }