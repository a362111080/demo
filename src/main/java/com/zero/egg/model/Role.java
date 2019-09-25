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
import java.util.List;

@ApiModel(value="角色")
@Data
@TableName(value = "base_role")
public class Role implements Serializable {

    private static final long serialVersionUID = -1352628950592203344L;

    @ApiModelProperty(value="主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value="编号")
    private String code;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="描述")
    private String description;

    @ApiModelProperty(value="创建人")
    private String creator;

    @ApiModelProperty(value="创建时间")
    private Date createtime;

    @ApiModelProperty(value="修改人")
    private String modifier;

    @ApiModelProperty(value="修改时间")
    private Date modifytime;

    @ApiModelProperty(value="逻辑删除标记")
    private Boolean dr = false;

    @TableField(exist = false)
    private List<Resource> resourceList;

}