package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.TaskEnums;
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
 * @since 2019-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_task")
@ApiModel(value="Task对象", description="")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "客商主键",required=false)
    private String supplierId;

    @ApiModelProperty(value = "客商名称",required=false)
    private String suppliername;

    @ApiModelProperty(value = "方案id",required=false)
    private String programId;

    @ApiModelProperty(value = "方案名称",required=false)
    private String programName;


    @ApiModelProperty(value = "品种名称",required=false)
    private String categoryname;


    @ApiModelProperty(value = "设备号",required=false)
    private String  equipmentNo;

    @ApiModelProperty(value = "状态",required=false)
    private String status;
    
    @ApiModelProperty(value = "类型",hidden=true)
    private String type;

    @ApiModelProperty(value = "备注",required=false)
    private String remark;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    
    public String getStatusName() {
		return TaskEnums.Status.note(Integer.parseInt(this.status));
	}
    
    public String  getTypeName() {
		return TaskEnums.Type.note(Integer.parseInt(this.type));
	}


    @TableField(exist = false)
    @ApiModelProperty(value = "卸货任务结束使用",hidden=true)
    private String cussupid;

    @TableField(exist = false)
    @ApiModelProperty(value = "卸货任务结束使用",hidden=true)
    public List<BillDetails>  UnloadDetails;

}
