package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.CompanyEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_company")
@ApiModel(value="Company对象", description="")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    @ApiModelProperty(value = "名称",required=false)
    private String name;

    @ApiModelProperty(value = "状态",hidden=true)
    private String status;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;
   
    @ApiModelProperty(value = "创建时间",hidden=true)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;
   
    @ApiModelProperty(value = "更新时间",hidden=true)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    @ApiModelProperty(value = "开始时间",hidden=true)
    private String begintime;



    @ApiModelProperty(value = "结束时间",hidden=true)
    private String endtime;


    /**
     * 前端传来的id数组(批量操作)
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "批量删除",hidden=true)
    private String ids;


    public String getStatusName() {
		return CompanyEnums.Status.note(Integer.parseInt(this.status));
	}

}
