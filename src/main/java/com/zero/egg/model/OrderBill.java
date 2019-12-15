package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel(value = "com.zero.egg.model.OrderBill")
@Data
@Accessors(chain = true)
@TableName(value = "order_order")
public class OrderBill implements Serializable {

    private static final long serialVersionUID = -6447035890501463080L;

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "null")
    private String id;

    /**
     * 企业id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业id")
    private String companyId;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    /**
     * 店铺名
     */
    @TableField(value = "shop_name")
    @ApiModelProperty(value = "店铺名")
    private String shopName;

    /**
     * 订货平台用户表的用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "订货平台用户表的用户ID")
    private String userId;

    /**
     * 订单编号
     */
    @TableField(value = "order_sn")
    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    /**
     * 订单状态
     */
    @TableField(value = "order_status")
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    /**
     * 收货地址id
     */
    @TableField(value = "address_id")
    @ApiModelProperty(value = "收货地址id")
    private String addressId;

    /**
     * 收货人名称
     */
    @TableField(value = "consignee")
    @ApiModelProperty(value = "收货人名称")
    private String consignee;

    /**
     * 收货人手机号
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    /**
     * 收货具体地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "收货具体地址")
    private String address;

    /**
     * 用户订单留言
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "用户订单留言")
    private String message;

    /**
     * 商品总费用
     */
    @TableField(value = "total_price")
    @ApiModelProperty(value = "商品总费用")
    private BigDecimal totalPrice;

    /**
     * 订单关闭时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "订单关闭时间")
    private Date endTime;

    /**
     * 创建者
     */
    @TableField(value = "creator")
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    /**
     * 更新人
     */
    @TableField(value = "modifier")
    @ApiModelProperty(value = "更新人")
    private String modifier;

    /**
     * 更新时间
     */
    @TableField(value = "modifytime")
    @ApiModelProperty(value = "更新时间")
    private Date modifytime;

    /**
     * 逻辑删除
     */
    @TableField(value = "dr")
    @ApiModelProperty(value = "逻辑删除")
    private Boolean dr = false;


    @TableField(exist = false)
    @ApiModelProperty(value = "订单明细", required = false)
    private List<OrderBillDetail> OrderDetlList;


    @TableField(value = "accept_status")
    @ApiModelProperty(value = "受理状态", required = false)
    private String acceptStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "客户名", required = false)
    private String customerName;

    @TableField(exist = false)
    @ApiModelProperty(value = "客户id", required = false)
    private String customerId;
}
