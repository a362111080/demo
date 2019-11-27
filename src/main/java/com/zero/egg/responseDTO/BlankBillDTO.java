package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName BlankBillDTO
 * @Description 空账单DTO
 * @Author lyming
 * @Date 2019/5/31 08:22
 **/
@Data
public class BlankBillDTO implements Serializable {

    private static final long serialVersionUID = -1482273599790147295L;

    @ApiModelProperty(value = "账单id", required = false)
    private String billId;

    @ApiModelProperty(value = "账单编号", required = false)
    private String billNo;

    @ApiModelProperty(value = "合作商id", required = false)
    private String customerId;

    @ApiModelProperty(value = "合作商店铺名", required = false)
    private String customerName;

    @ApiModelProperty(value = "品种id", required = false)
    private String categoryId;

    @ApiModelProperty(value = "品种名", required = false)
    private String categoryName;

    @ApiModelProperty(value = "所属方案id", required = false)
    private String programId;

    @ApiModelProperty(value = "方案细节(规格)id", required = false)
    private String specificationId;

    @ApiModelProperty(value = "标记方式", required = false)
    private String marker;

    @ApiModelProperty(value = "计重方式(1:去皮 2:包)", required = false)
    private Integer mode;

    @ApiModelProperty(value = "实际计重方式",required = false)
    private Integer currentMode;

    @ApiModelProperty(value = "去皮时,实重总和", required = false)
    private BigDecimal totalWeight;

    @ApiModelProperty(value = "去皮值(仅用于包转去皮时使用)", required = false)
    private BigDecimal numberical;

    @ApiModelProperty(value = "去皮值(仅用于去皮进货时所选方案的去皮值)", required = false)
    private BigDecimal numbericalBefore;

    @ApiModelProperty(value = "数量", required = false)
    private Long quantity;

    @ApiModelProperty(value = "单价", required = false)
    private BigDecimal price;

    @ApiModelProperty(value = "小计", required = false)
    private BigDecimal subTotal;

    @ApiModelProperty(value = "没去皮时,实重总和", required = false)
    private BigDecimal totalWeightBefore;
}
