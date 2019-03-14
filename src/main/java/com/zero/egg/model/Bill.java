package com.zero.egg.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.BillEnums;
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
 * @since 2019-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_bill")
@ApiModel(value="Bill对象", description="")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "账单编号",required=false)
    private String billNo;

    @ApiModelProperty(value = "客商主键",required=false)
    private String cussupId;

    @ApiModelProperty(value = "账单日期",required=false)
    private LocalDateTime billDate;

    @ApiModelProperty(value = "账单类型",required=false)
    private String type;

    @ApiModelProperty(value = "数量",required=false)
    private BigDecimal quantity;

    @ApiModelProperty(value = "金额",required=false)
    private BigDecimal amount;

    @ApiModelProperty(value = "状态",hidden=true)
    private String status;

    @ApiModelProperty(value = "备注",required=false)
    private String remark;

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
		return BillEnums.Status.note(Integer.parseInt(this.status));
	}
    
}
