package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.UserEnums;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_user")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "编码",required=false)
    private String code;

    @ApiModelProperty(value = "姓名",required=false)
    private String name;

    @ApiModelProperty(value = "性别",required=false)
    private Integer sex;

    @ApiModelProperty(value = "联系方式",required=false)
    private String phone;

    @ApiModelProperty(value = "密码",hidden=true)
    private String password;

    @ApiModelProperty(value = "状态",hidden=true)
    private String status;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private LocalDateTime createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private LocalDateTime modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    
    public String getStatusName() {
		return UserEnums.Status.note(Integer.parseInt(this.status));
	}

}