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
 * @since 2019-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_goods")
@ApiModel(value="Goods对象", description="")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键")
    private String shopId;

    @ApiModelProperty(value = "企业主键")
    private String companyId;

    @ApiModelProperty(value = "供应商主键")
    private String supplierId;

    @ApiModelProperty(value = "规格主键")
    private String specificationId;

    @ApiModelProperty(value = "商品分类主键")
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品编码")
    private String goodsNo;

    @ApiModelProperty(value = "标记")
    private String marker;

    @ApiModelProperty(value = "记重方式")
    private String mode;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    @ApiModelProperty(value = "更新人")
    private String modifier;

    @ApiModelProperty(value = "更新时间")
    private Date modifytime;

    @ApiModelProperty(value = "删除标识")
    private Boolean dr;

    @TableField(exist = false)
    @ApiModelProperty(value = "客户名称",hidden=true)
    public String customername;

    @TableField(exist = false)
    @ApiModelProperty(value = "品种名称",hidden=true)
    public String categoryname;

    @TableField(exist = false)
    @ApiModelProperty(value = "账单号",hidden=true)
    public String billNo;

    @TableField(exist = false)
    @ApiModelProperty(value = "任务id",hidden=true)
    public String taskId;

    @TableField(exist = false)
    @ApiModelProperty(value = "扫码显示标识",hidden=true)
    public String brokenmarker;

}
