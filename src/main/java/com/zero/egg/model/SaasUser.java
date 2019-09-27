package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value="com.zero.egg.model.SaasUser")
@Data
public class SaasUser implements Serializable {
    /**
    * 主键id
    */
    @ApiModelProperty(value="主键id")
    private String id;

    /**
    * 登录名
    */
    @ApiModelProperty(value="登录名")
    private String loginname;

    /**
    * 登录密码
    */
    @ApiModelProperty(value="登录密码")
    private String password;

    @TableField(exist = false)
    @ApiModelProperty(value = "权限列表",hidden=true)
    private List<Resource> resources;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色列表",hidden=true)
    private List<Role> roles;

    private static final long serialVersionUID = 1L;
}