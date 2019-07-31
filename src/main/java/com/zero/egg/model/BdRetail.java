package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 零售账单细节
 */
@ApiModel(value="com.zero.egg.model.BdRetail")
@Data
@TableName(value = "bd_retail")
public class BdRetail implements Serializable {
    /**
     * 主键
     */
     @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 所属店铺
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="所属店铺")
    private String shopId;

    /**
     * 企业主键
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value="企业主键")
    private String companyId;

    /**
     * 账单主键
     */
    @TableField(value = "bill_id")
    @ApiModelProperty(value="账单主键")
    private String billId;

    /**
     * 单价
     */
    @TableField(value = "price")
    @ApiModelProperty(value="单价")
    private BigDecimal price;

    /**
     * 数量
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="数量")
    private Integer quantity;

    /**
     * 数量方式(1:斤 2:个 3:盘)
     */
    @TableField(value = "quantity_mode")
    @ApiModelProperty(value="数量方式(1:斤 2:个 3:盘)")
    private Integer quantityMode;

    /**
     * 金额
     */
    @TableField(value = "amount")
    @ApiModelProperty(value="金额")
    private BigDecimal amount;

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
     * 更新人
     */
    @TableField(value = "modifier")
    @ApiModelProperty(value="更新人")
    private String modifier;

    /**
     * 更新时间
     */
    @TableField(value = "modifytime")
    @ApiModelProperty(value="更新时间")
    private Date modifytime;

    /**
     * 删除标识
     */
    @TableField(value = "dr")
    @ApiModelProperty(value="删除标识")
    private Boolean dr;

    private static final long serialVersionUID = 1L;

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_COMPANY_ID = "company_id";

    public static final String COL_BILL_ID = "bill_id";

    public static final String COL_PRICE = "price";

    public static final String COL_QUANTITY = "quantity";

    public static final String COL_QUANTITY_MODE = "quantity_mode";

    public static final String COL_AMOUNT = "amount";

    public static final String COL_CREATOR = "creator";

    public static final String COL_CREATETIME = "createtime";

    public static final String COL_MODIFIER = "modifier";

    public static final String COL_MODIFYTIME = "modifytime";

    public static final String COL_DR = "dr";
}