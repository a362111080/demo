package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@TableName("bd_bill_details")
@ApiModel(value="BillDetails对象", description="")
public class BillDetails  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "账单主键",required=false)
    private String billId;

    @ApiModelProperty(value = "商品分类",required=false)
    private String goodsCategoryId;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称",hidden=true)
    public String goodsCategoryName;

    @ApiModelProperty(value = "规格主键",required=false)
    private String specificationId;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格标识",hidden=true)
    public String marker;

    @ApiModelProperty(value = "方案主键",required=false)
    private String programId;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格名称",hidden=true)
    public String programName;

    @ApiModelProperty(value = "单价",required=false)
    private BigDecimal price;

    @ApiModelProperty(value = "数量",required=false)
    private BigDecimal quantity;

    @ApiModelProperty(value = "实际计重方式",required = false)
    private Integer currentMode;

    @ApiModelProperty(value = "去皮时重量",required=false)
    private BigDecimal totalWeight;

    @ApiModelProperty(value = "去皮前重量",required=false)
    private BigDecimal totalWeightBefore;

    @ApiModelProperty(value = "去皮值(仅用于包转去皮时使用)", required = false)
    private BigDecimal numberical;

    @ApiModelProperty(value = "金额",required=false)
    private BigDecimal amount;

    @ApiModelProperty(value = "备注",required=false)
    private String remark;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建人名称",hidden=true)
    private String createname;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    @TableField(exist = false)
    @ApiModelProperty(value = "计重方式(1:去皮 2:包)",hidden=true)
    private Integer mode;


}
