package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName Category
 * @Description 鸡蛋类型
 * @Author lyming
 * @Date 2018/11/1 16:22
 **/
@Data
@TableName(value = "bd_category")
@ApiModel(value="鸡蛋类型对象", description="")
public class Category {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键",required=false)
    private String id;

    /**
     * 鸡蛋类别名称
     */
    @ApiModelProperty(value = "鸡蛋类别名称",required=false)
    private String name;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人",required=false)
    private String creator;

    /**
     * 类别创建时间
     */
    @ApiModelProperty(value = "类别创建时间",required=false)
    private Date createtime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人",required=false)
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required=false)
    private Date modifytime;


    /**
     * 所属店铺id
     */
    @ApiModelProperty(value = "所属店铺id",required=false)
    private String shopId;

    /**
     * 店铺所属企业id
     */
    @ApiModelProperty(value = "店铺所属企业id",required=false)
    private String companyId;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    @ApiModelProperty(value = "删除标识 0:未删除(默认) 1:已删除",required=false)
    private Integer dr = 0;
}
