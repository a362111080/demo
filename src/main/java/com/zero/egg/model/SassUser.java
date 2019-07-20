package com.zero.egg.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="com.zero.egg.model.SassUser")
@Data
public class SassUser implements Serializable {
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

    private static final long serialVersionUID = 1L;
}