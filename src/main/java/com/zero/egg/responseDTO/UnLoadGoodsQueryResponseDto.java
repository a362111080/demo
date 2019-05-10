package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

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
     * 任务id
     */
    @ApiModelProperty(value = "方案名")
    private String programname;


    /**
     * 任务id
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;


    /**
     * 当前方案合计卸货数量
     */
    @ApiModelProperty(value = "当前方案合计卸货数量")
    private BigDecimal sumVal;

}
