package com.zero.egg.responseDTO;

import com.zero.egg.model.OrderBillDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderBillDetailResponseDTO
 * @Description 订货平台账单细节ResponseDTO
 * @Author lyming
 * @Date 2019/10/12 7:11 下午
 **/
@Data
public class OrderBillDetailResponseDTO implements Serializable {

    private static final long serialVersionUID = -4745309094403989817L;

    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private String companyId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    /**
     * 店铺名
     */
    @ApiModelProperty(value = "店铺名")
    private String shopName;

    /**
     * 订货平台用户表的用户ID
     */
    @ApiModelProperty(value = "订货平台用户表的用户ID")
    private String userId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    /**
     * 收货地址id
     */
    @ApiModelProperty(value = "收货地址id")
    private String addressId;

    /**
     * 收货人名称
     */
    @ApiModelProperty(value = "收货人名称")
    private String consignee;

    /**
     * 收货人手机号
     */
    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    /**
     * 收货具体地址
     */
    @ApiModelProperty(value = "收货具体地址")
    private String address;

    /**
     * 用户订单留言
     */
    @ApiModelProperty(value = "用户订单留言")
    private String message;

    /**
     * 商品总费用
     */
    @ApiModelProperty(value = "商品总费用")
    private BigDecimal totalPrice;

    /**
     * 订单关闭时间
     */
    @ApiModelProperty(value = "订单关闭时间")
    private Date endTime;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String modifier;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date modifytime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除")
    private Boolean dr = false;

    /**
     * 订单细节列表
     */
    private List<OrderBillDetail> orderBillDetailList;
}
