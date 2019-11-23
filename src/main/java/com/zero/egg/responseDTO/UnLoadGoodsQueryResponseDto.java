package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UnLoadGoodsQueryResponseDto implements Serializable {

    private static final long serialVersionUID = 6208392739302336182L;

    /**
     * 鸡蛋类别id
     */
    @ApiModelProperty(value = "鸡蛋类别id")
    private String id;
    /**
     * 鸡蛋类别名称
     */
    @ApiModelProperty(value = "鸡蛋类别名称")
    private String name;

    @ApiModelProperty(value = "鸡蛋类别id")
    private String goodsCategoryId;

    /**
     * 方案标识
     */
    @ApiModelProperty(value = "方案标识")
    private String marker;


    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;


    /**
     * 任务状态
     */
    @ApiModelProperty(value = "任务状态")
    private String status;



    /**
     * 任务id
     */
    @ApiModelProperty(value = "方案id")
    private String programId;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "方案名")
    private String programname;



    /**
     * 当前方案合计卸货数量
     */
    @ApiModelProperty(value = "当前方案合计卸货数量")
    private BigDecimal sumVal;

    /**
     * 当前方案预警卸货数量
     */
    @ApiModelProperty(value = "当前方案合计卸货数量")
    private BigDecimal warNum;


    /**
     * 当前方案预警卸货数量
     */
    @ApiModelProperty(value = "当前方案合计卸货斤两")
    private BigDecimal sumweight;

    /**
     * 当前方案预警卸货数量
     */
    @ApiModelProperty(value = "称重方式")
    private String mode;

    @ApiModelProperty(value = "规格id")
    private String  specificationId;
}
