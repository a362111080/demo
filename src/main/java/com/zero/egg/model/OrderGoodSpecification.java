package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value="订货商品对象", description="CQ")
public class OrderGoodSpecification implements Serializable {

    private static final long serialVersionUID = -4156568639256890479L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 供关联店铺
     */
    @ApiModelProperty(value = "商品id",required=false)
    private  String goodsId;

    @ApiModelProperty(value = "商品规格名称",required=false)
    private  String specification;

    @ApiModelProperty(value = "商品规格值",required=false)
    private  String value;

    @ApiModelProperty(value = "商品规格图片",required=false)
    private  String pic_url;

    @ApiModelProperty(value="修改人")
    private String modifier;


    @ApiModelProperty(value="创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;


    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr = false;
}
