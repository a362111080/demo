package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "bd_supplier")
@ApiModel(value = "Supplier对象", description = "")
public class Supplier implements Serializable {

    private static final long serialVersionUID = -5449241058403175052L;
    /**
     * @ClassName 供应商类
     * @Description 供应商类
     * @Author CQ
     * @Date 2018/11/57
     **/


    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键", required = false)
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 供应商关联店铺
     */
    @ApiModelProperty(value = "店铺主键", required = false)
    @TableField(value = "shop_id")
    private String shopid;
    /**
     * 供应商关联企业
     */
    @ApiModelProperty(value = "企业主键", required = false)
    @TableField(value = "company_id")
    private String companyid;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "企业主键", required = false)
    private String code;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称", required = false)
    private String name;

    /**
     * 供应商简称
     */
    @ApiModelProperty(value = "供应商简称", required = false)
    @TableField(value = "short_name")
    private String shortname;

    /**
     * 供应商类型
     */
    @ApiModelProperty(value = "供应商类型", required = false)
    private String type;

    /**
     * 供应商联系方式
     */
    @ApiModelProperty(value = "供应商联系方式", required = false)
    private String phone;


    /**
     * 供应商联系人
     */
    @ApiModelProperty(value = "供应商联系人", required = false)
    private String linkman;


    /**
     * 供应商备注
     */
    @ApiModelProperty(value = "供应商备注", required = false)
    private String remark;


    /**
     * 状态 0:停用 1:启用(默认)
     */
    @ApiModelProperty(value = "供应商状态", required = false)
    private String status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "供应商创建人", required = false)
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "供应商创建时间", required = false)
    private Date createtime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "供应商修改人", required = false)
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", required = false)
    private Date modifytime;

    /**
     * 省市区id
     */
    @ApiModelProperty(value = "省市区id", required = false)
    @TableField(value = "city_id")
    private String cityid;

    /**
     * 省市区一级id
     */
    @ApiModelProperty(value = "省市区一级id", required = false)
    @TableField(exist = false)
    private String city1;
    /**
     * 省市区二级id
     */
    @ApiModelProperty(value = "省市区二级id", required = false)
    @TableField(exist = false)
    private String city2;


    @ApiModelProperty(value = "称重方式",required=false)
    private String weightMode;


}

