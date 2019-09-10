package com.zero.egg.requestDTO;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName SaveRetailDeatilsRequestDTO
 * @Description 新增一条零售账单细节DTO
 * @Author lyming
 * @Date 2019-08-01 17:11
 **/
@Data
public class SaveRetailDeatilsRequestDTO implements Serializable {

    private static final long serialVersionUID = -6768290314546811924L;

    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 所属店铺
     */
    @ApiModelProperty(value="所属店铺")
    private String shopId;

    /**
     * 企业主键
     */
    @ApiModelProperty(value="企业主键")
    private String companyId;

    /**
     * 账单主键
     */
    @ApiModelProperty(value="账单主键")
    @NotNull(message = "账单id不能为空")
    private String billId;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    /**
     * 数量
     */
    @ApiModelProperty(value="数量")
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    /**
     * 数量方式(1:斤 2:个 3:盘)
     */
    @ApiModelProperty(value="数量方式(1:斤 2:个 3:盘)")
    @NotNull(message = "数量方式不能为空")
    private Integer quantityMode;

    /**
     * 应收金额
     */
    @ApiModelProperty(value="应收金额")
    private BigDecimal amount;

    /**
     * 实收金额
     */
    @ApiModelProperty(value="实收金额")
    @NotNull(message = "实收金额不能为空")
    private BigDecimal realAmount;
    /**
     * 创建人
     */
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
    @ApiModelProperty(value="更新人")
    private String modifier;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date modifytime;

    /**
     * 删除标识
     */
    @ApiModelProperty(value="删除标识")
    private Boolean dr = false;
}
