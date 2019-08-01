package com.zero.egg.requestDTO;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private String billId;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价")
    private BigDecimal price;

    /**
     * 数量
     */
    @ApiModelProperty(value="数量")
    private Integer quantity;

    /**
     * 数量方式(1:斤 2:个 3:盘)
     */
    @ApiModelProperty(value="数量方式(1:斤 2:个 3:盘)")
    private Integer quantityMode;

    /**
     * 金额
     */
    @ApiModelProperty(value="金额")
    private BigDecimal amount;

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
