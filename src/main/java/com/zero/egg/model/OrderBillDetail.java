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

@ApiModel(value="com.zero.egg.model.OrderBiilDetail")
@Data
@Accessors(chain = true)
@TableName(value = "order_order_detail")
public class OrderBillDetail implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="null")
    private String id;

    /**
     * 所属订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value="所属订单id")
    private String orderId;

    /**
     * 企业id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value="企业id")
    private String companyId;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="店铺id")
    private String shopId;

    /**
     * 购物车商品id
     */
    @TableField(value = "cart_id")
    @ApiModelProperty(value="购物车商品id")
    private String cartId;
    /**
     * 订单细节品种id
     */
    @TableField(value = "order_category_id")
    @ApiModelProperty(value="订单细节品种id")
    private String orderCategoryId;


    /**
     * 品种名
     */
    @TableField(value = "order_category_name")
    @ApiModelProperty(value="品种名")
    private String orderCategoryName;

    /**
     * 规格id
     */
    @TableField(value = "specification_id")
    @ApiModelProperty(value="规格id")
    private String specificationId;

    /**
     * 规格名
     */
    @TableField(value = "specification_name")
    @ApiModelProperty(value="规格名")
    private String specificationName;

    /**
     * 规格值
     */
    @TableField(value = "specification_value")
    @ApiModelProperty(value="规格值")
    private String specificationValue;

    /**
     * 商品单价
     */
    @TableField(value = "goods_price")
    @ApiModelProperty(value="商品单价")
    private BigDecimal goodsPrice;

    /**
     * 商品数量
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="商品数量")
    private BigDecimal quantity;

    /**
     * 商品小计
     */
    @TableField(value = "subtotal")
    @ApiModelProperty(value="商品小计")
    private BigDecimal subtotal;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    @ApiModelProperty(value="创建时间")
    private Date createtime;

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


    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称",required=false)
    private String goodsName;

    @TableField(exist = false)
    @ApiModelProperty(value = "计重方式",required=false)
    private String weightMode;


    private static final long serialVersionUID = 1L;
}
