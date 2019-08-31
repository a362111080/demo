package com.zero.egg.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_category")
@ApiModel(value="商品分类", description="CQ")
public class OrderCategory implements Serializable {
    private static final long serialVersionUID = 5563815649024060975L;


    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 供关联店铺
     */
    @ApiModelProperty(value = "店铺主键",required=false)
    private  String shopid;
    /**
     * 供应商关联企业
     */
    @ApiModelProperty(value = "企业主键",required=false)
    private  String companyid;

    @ApiModelProperty(value = "分类名称",required=false)
    private  String name;


    @ApiModelProperty(value = "层级",required=false)
    private  String level;


    @ApiModelProperty(value = "排序",required=false)
    private  Integer sortOrder;

    @ApiModelProperty(value = "父级ID",required=false)
    private  String pid;

    @ApiModelProperty(value = "类目图标",required=false)
    private  String iconUrl;

    @ApiModelProperty(value = "类目图片",required=false)
    private  String picUrl;


    @ApiModelProperty(value = "修改时间",required=false)
    private Date modifytime;


    @ApiModelProperty(value = " 创建时间",required=false)
    private Date createtime;

}
