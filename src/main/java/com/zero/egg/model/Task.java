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
    private String cussupId;

    @ApiModelProperty(value = "客商名称",required=false)
    @TableField(exist = false)
    private String cussupName;

    @TableField(exist = false)
    @ApiModelProperty(value = "方案id",required=false)
    private String programId;

    @ApiModelProperty(value = "方案名称",required=false)
    @TableField(exist = false)
    private String programName;


    @TableField(exist = false)
    @ApiModelProperty(value = "品种id",required=false)
    private String categoryId;

    @TableField(exist = false)
    @ApiModelProperty(value = "品种名称",required=false)
    private String categoryname;


    @ApiModelProperty(value = "设备号",required=false)
    private String  equipmentNo;

    @ApiModelProperty(value = "状态( 0:执行中  1：已取消  2：已完成   3：已暂停)", required = false)
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

    @ApiModelProperty(value = "订货平台订单id",hidden=true)
    private String  orderId;

    public String getStatusName() {
		return TaskEnums.Status.note(Integer.parseInt(this.status));
	}

    public String  getTypeName() {
		return TaskEnums.Type.note(Integer.parseInt(this.type));
	}


    public String getCussupId() {
        return cussupId;
    }

    public void setCussupId(String cussupId) {
        this.cussupId = cussupId;
    }


    @TableField(exist = false)
    @ApiModelProperty(value = "更好任务方案使用",hidden=true)
    private String newProgram;

    @TableField(exist = false)
    @ApiModelProperty(value = "卸货任务结束使用",hidden=true)
    public List<BillDetails>  UnloadDetails;

    @TableField(exist = false)
    @ApiModelProperty(value = "操作前任务状态",hidden=true)
    private String statusBefore;

    @TableField(exist = false)
    @ApiModelProperty(value = "订货平台订单编号",hidden=true)
    private String  orderSn;

    @TableField(exist = false)
    @ApiModelProperty(value = "订货平台用户id",hidden=true)
    private String  orderUserId;

    @TableField(exist = true)
    @ApiModelProperty(value = "是否称重 0:不称重 1:称重",hidden=true)
    private Integer isWeight ;
}
