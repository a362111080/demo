package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName 客户类
 * @Description 客户类
 * @Author CQ
 * @Date 2019/02/27
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "bd_customer")
@ApiModel(value="Customer对象", description="")
public class Customer {


    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键",required=false)
    @TableId(type = IdType.UUID)
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

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "客户编码",required=false)
    private String code;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "客户名称",required=false)
    private String name;

    /**
     * 供应商简称
     */
    @ApiModelProperty(value = " 客户简称",required=false)
    private String shortname;

    /**
     * 供应商类型
     */
    @ApiModelProperty(value = " 客户类型",required=false)
    private String type;

    /**
     * 供应商联系方式
     */
    @ApiModelProperty(value = " 客户联系方式",required=false)
    private String phone;


    /**
     * 供应商联系人
     */
    @ApiModelProperty(value = " 客户联系人",required=false)
    private String linkman;


    /**
     * 供应商备注
     */
    @ApiModelProperty(value = " 客户备注",required=false)
    private String remark;


    /**
     * 状态 0:停用 1:启用(默认)
     */
    @ApiModelProperty(value = " 客户状态",required=false)
    private String status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "客户创建人",required=false)
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = " 创建时间",required=false)
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
     * 省市区id
     */
    @ApiModelProperty(value = "省市区id（最后一级id）",required=false)
    private String cityid;
    /**
     * 省市区一级id
     */
    @ApiModelProperty(value = "省市区一级id",required=false)
    private String city1;
    /**
     * 省市区二级id
     */
    @ApiModelProperty(value = "省市区二级id",required=false)
    private String city2;

    /**
     * 合作商计重习惯(1:包->按箱收费 2:去皮->按斤收费)
     * 如果按箱出货,所有货物默认按箱计算小计,如果按去皮出货,去皮卸的货按斤计算小计,包卸的货,默认还是按箱
     * 包卸的货,出货的时候,如果要转换成按斤计算小计,如要额外输入去皮值
     */
    private Integer weightMode;
}
