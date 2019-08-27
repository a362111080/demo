package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="com.zero.egg.model.OrderCart")
@Data
@TableName(value = "order_cart")
public class OrderCart implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

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
     * 用户表的用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户表的用户ID")
    private Integer userId;

    /**
     * 商品表的商品ID
     */
    @TableField(value = "goods_id")
    @ApiModelProperty(value="商品表的商品ID")
    private Integer goodsId;

    /**
     * 商品编号
     */
    @TableField(value = "goods_sn")
    @ApiModelProperty(value="商品编号")
    private String goodsSn;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty(value="商品名称")
    private String goodsName;

    /**
     * 商品货品的价格
     */
    @TableField(value = "price")
    @ApiModelProperty(value="商品货品的价格")
    private BigDecimal price;

    /**
     * 商品货品的数量
     */
    @TableField(value = "number")
    @ApiModelProperty(value="商品货品的数量")
    private Short number;

    /**
     * 商品单位，例如1:斤、2:箱
     */
    @TableField(value = "unit")
    @ApiModelProperty(value="商品单位，例如1:斤、2:箱")
    private String unit;

    /**
     * 购物车中商品是否选择状态
     */
    @TableField(value = "checked")
    @ApiModelProperty(value="购物车中商品是否选择状态")
    private Boolean checked;

    /**
     * 商品图片或者商品货品图片
     */
    @TableField(value = "pic_url")
    @ApiModelProperty(value="商品图片或者商品货品图片")
    private String picUrl;

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
    private Boolean dr;

    private static final long serialVersionUID = 1L;
}