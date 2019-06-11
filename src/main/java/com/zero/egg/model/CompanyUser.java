package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.CompanyUserEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_company_user")
@ApiModel(value="CompanyUser对象", description="")
public class CompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "编码",required=false)
    private String code;

    @ApiModelProperty(value = "名称",required=false)
    private String name;

    @ApiModelProperty(value = "联系方式",required=false)
    private String phone;

    @ApiModelProperty(value = "登陆名",required = false)
    private String loginname;

    @ApiModelProperty(value = "密码",hidden=true)
    private String password;

    @ApiModelProperty(value = "状态",hidden=true)
    private String status;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @TableField(exist = false)
    @ApiModelProperty(value = "开始时间",hidden=true)
    private Date begintime;


    @TableField(exist = false)
    @ApiModelProperty(value = "结束时间",hidden=true)
    private Date endtime;



    @TableField(exist = false)
    @ApiModelProperty(value = "企业名称",hidden=true)
    private String companyName;

    @TableField(exist = false)
    @ApiModelProperty(value = "企业店铺信息",hidden=true)
    private List<Shop> shopList;


    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    public String getStatusName() {
		return CompanyUserEnums.Status.note(Integer.parseInt(this.status));
	}
}
