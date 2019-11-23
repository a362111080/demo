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

@ApiModel(value = "资源")
@Data
@TableName(value = "base_resource")
public class Resource implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
    private String id;

    @TableField(value = "name")
    @ApiModelProperty(value = "资源名")
    private String name;

    @TableField(value = "content")
    @ApiModelProperty(value = "备注")
    private String content;

    @TableField(value = "icon")
    @ApiModelProperty(value = "图标")
    private String icon;

    @TableField(value = "type")
    @ApiModelProperty(value = "类型")
    private String type;

    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父级资源id")
    private String parentId;

    @TableField(value = "index")
    @ApiModelProperty(value = "角标")
    private Integer index;

    @TableField(value = "creator")
    @ApiModelProperty(value = "创建者")
    private String creator;

    @TableField(value = "createtime")
    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    @TableField(value = "modifier")
    @ApiModelProperty(value = "修改者")
    private String modifier;

    @TableField(value = "modifytime")
    @ApiModelProperty(value = "修改时间")
    private Date modifytime;

    @TableField(value = "dr")
    @ApiModelProperty(value = "逻辑删除标识")
    private Boolean dr = false;

    private static final long serialVersionUID = 1L;

    public static final String COL_NAME = "name";

    public static final String COL_CONTENT = "content";

    public static final String COL_ICON = "icon";

    public static final String COL_TYPE = "type";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_INDEX = "index";

    public static final String COL_CREATOR = "creator";

    public static final String COL_CREATETIME = "createtime";

    public static final String COL_MODIFIER = "modifier";

    public static final String COL_MODIFYTIME = "modifytime";

    public static final String COL_DR = "dr";
}