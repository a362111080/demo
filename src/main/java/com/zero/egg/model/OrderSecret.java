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
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_secret")
@ApiModel(value="秘钥对象", description="CQ")
public class OrderSecret implements Serializable {
    private static final long serialVersionUID = -5491994207717590826L;


    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 关联店铺
     */
    @ApiModelProperty(value = "店铺主键",required=false)
    @TableField("shop_id")
    private  String shopid;

    @ApiModelProperty(value = "企业主键",required=false)
    @TableField("company_id")
    private  String companyid;



    @ApiModelProperty(value = "客户主键",required=false)
    private  String customerId;



    @ApiModelProperty(value = "企业主键",required=false)
    private  String secretKey;

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

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;


    @TableField(exist = false)
    @ApiModelProperty(value = "绑定时间",required=false)
    public String bindtime;

    @TableField(exist = false)
    @ApiModelProperty(value = "绑定人",required=false)
    public String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "联系方式",required=false)
    public String phone;


    @TableField(exist = false)
    @ApiModelProperty(value = "绑定人类型",required=false)
    public String type;


    @ApiModelProperty(value = "绑定人状态",required=false)
    public Boolean status;

    @TableField(exist = false)
    @ApiModelProperty(value = "客户主键",required=false)
    private  String customername;

    /**
     * 前端传来的id数组(批量删除)
     */
    @TableField(exist = false)
    private List<String> ids;

}
