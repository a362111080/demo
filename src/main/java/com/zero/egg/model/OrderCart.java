package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "order_cart")
@Accessors(chain = true)
public class OrderCart implements Serializable {

    private static final long serialVersionUID = -7800820035104760775L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private String shopId;

    /**
     * 用户表的用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 该商品已经选择的规格id
     */
    @TableField(value = "good_specification_id")
    private String goodSpecificationId;

    /**
     * 已选规格名
     */
    @TableField(value = "good_specification_name")
    private String goodSpecificationName;

    /**
     * 已选规格值
     */
    @TableField(value = "good_specification_value")
    private String goodSpecificationValue;

    /**
     * 合作商计重习惯(1:去皮->按斤收费 2:包->按箱收费)
     * 如果按箱出货,所有货物默认按箱计算小计,如果按去皮出货,去皮卸的货按斤计算小计,包卸的货,默认还是按箱
     * 包卸的货,出货的时候,如果要转换成按斤计算小计,如要额外输入去皮值
     */
    @TableField(value = "weight_mode")
    private String weightMode;

    /**
     * 商品表的商品ID
     */
    @TableField(value = "goods_id")
    private String goodsId;

    /**
     * 商品编号
     */
    @TableField(value = "goods_sn")
    private String goodsSn;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 商品货品的价格
     */
    @TableField(value = "price")
    private String price;

    /**
     * 商品货品的数量
     */
    @TableField(value = "number")
    private Integer number;

    /**
     * 购物车中商品是否选择状态
     */
    @TableField(value = "checked")
    private Boolean checked;

    /**
     * 商品图片或者商品货品图片
     */
    @TableField(value = "pic_url")
    private String picUrl;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    private Date createtime;

    /**
     * 更新时间
     */
    @TableField(value = "modifytime")
    private Date modifytime;

    /**
     * 逻辑删除
     */
    @TableField(value = "dr")
    private Boolean dr;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品规格列表",hidden=true)
    private List<OrderGoodsSpecification> sepcificationList;
}