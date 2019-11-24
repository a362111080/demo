package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "order_goods_specification")
public class OrderGoodsSpecification implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private String goodsId;

    /**
     * 商品规格名称
     */
    @TableField(value = "specification")
    private String specification;

    /**
     * 商品规格值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 商品规格图片
     */
    @TableField(value = "pic_url")
    private String picUrl;

    /**
     * 商品规格图片,联查用
     */
    @TableField(exist = false)
    private String pic;

    /**
     * 创建人
     */
    @TableField(value = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    private Date createtime;

    /**
     * 修改人
     */
    @TableField(value = "modifier")
    private String modifier;

    @TableField(value = "modifytime")
    private Date modifytime;

    /**
     * 删除标识
     */
    @TableField(value = "dr")
    private Boolean dr;

    /**
     * 参考价格
     */
    @TableField(value = "price")
    private String price;

    private static final long serialVersionUID = 1L;
}
