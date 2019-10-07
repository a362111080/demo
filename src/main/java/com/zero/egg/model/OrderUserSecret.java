package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.zero.egg.model.OrderUserSecret")
@Data
@TableName(value = "order_user_secret")
@Accessors(chain =true)
public class OrderUserSecret implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 订货平台用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="订货平台用户id")
    private String userId;

    /**
     * 秘钥id
     */
    @TableField(value = "secret_id")
    @ApiModelProperty(value="秘钥id")
    private String secretId;

    /**
     * 创建人
     */
    @TableField(value = "creator")
    @ApiModelProperty(value="创建人")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    @ApiModelProperty(value="创建时间")
    private Date createtime;

    /**
     * 修改人
     */
    @TableField(value = "modifier")
    @ApiModelProperty(value="修改人")
    private String modifier;

    /**
     * 修改时间
     */
    @TableField(value = "modifytime")
    @ApiModelProperty(value="修改时间")
    private Date modifytime;

    /**
     * 逻辑删除标识
     */
    @TableField(value = "dr")
    @ApiModelProperty(value="逻辑删除标识")
    private Boolean dr;

    private static final long serialVersionUID = 1L;
}