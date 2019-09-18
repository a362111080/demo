package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value="订货商品对象", description="CQ")
public class OrderGoods implements Serializable {
    private static final long serialVersionUID = -6749838095642804942L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 供关联店铺
     */
    @ApiModelProperty(value = "店铺主键",required=false)
    private  String shopId;
    /**
     * 供应商关联企业
     */
    @ApiModelProperty(value = "企业主键",required=false)
    private  String companyId;


    @ApiModelProperty(value = "商品编号",required=false)
    private  String goodsSn;


    @ApiModelProperty(value = "商品名称",required=false)
    private  String name;


    @ApiModelProperty(value = "商品所属类目ID",required=false)
    private  String categoryId;

    @ApiModelProperty(value = "宣传图",required=false)
    private  String gallery;

    @ApiModelProperty(value = "商品关键字",required=false)
    private  String keywords;

    @ApiModelProperty(value = "商品简介",required=false)
    private  String brief;

    @ApiModelProperty(value = "是否上架",required=false)
    private  Boolean isOnSale;


    @ApiModelProperty(value = "类目图片",required=false)
    private  String picUrl;

    @ApiModelProperty(value = "计价单位",required=false)
    private  String unit;

    @ApiModelProperty(value = "价格",required=false)
    private BigDecimal price;

    @ApiModelProperty(value = "商品介绍",required=false)
    private String detail;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;


    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr = false;

}
