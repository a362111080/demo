package com.zero.egg.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserRole
 * @Description TODO
 * @Author lyming
 * @Date 2019/9/23 5:11 下午
 **/
@ApiModel(value="com.zero.egg.model.UserRole")
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = -6841884923470717874L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private Date modifytime;

    @ApiModelProperty(value = "逻辑删除标识")
    private Boolean dr = false;
}