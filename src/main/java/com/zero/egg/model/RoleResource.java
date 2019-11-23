package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="资源角色关系")
@Data
@TableName(value = "base_role_resource")
public class RoleResource implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    @TableField(value = "role_id")
    @ApiModelProperty(value="角色id")
    private String roleId;

    @TableField(value = "resource_id")
    @ApiModelProperty(value="资源id")
    private String resourceId;

    @TableField(value = "creator")
    @ApiModelProperty(value="创建人")
    private String creator;

    @TableField(value = "createtime")
    @ApiModelProperty(value="创建时间")
    private Date createtime;

    @TableField(value = "modifier")
    @ApiModelProperty(value="修改人")
    private String modifier;

    @TableField(value = "modifytime")
    @ApiModelProperty(value="修改时间")
    private Date modifytime;

    @TableField(value = "dr")
    @ApiModelProperty(value="逻辑删除标识")
    private Boolean dr = false;

    private static final long serialVersionUID = 1L;

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_RESOURCE_ID = "resource_id";

    public static final String COL_CREATOR = "creator";

    public static final String COL_CREATETIME = "createtime";

    public static final String COL_MODIFIER = "modifier";

    public static final String COL_MODIFYTIME = "modifytime";

    public static final String COL_DR = "dr";
}