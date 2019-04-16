package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UnLoadGoodsQueryResponseDto implements Serializable {

    private static final long serialVersionUID = 6208392739302336182L;

    /**
     * 鸡蛋类别id
     */
    private String id;
    /**
     * 鸡蛋类别名称
     */
    private String name;


    /**
     * 方案标识
     */
    private String marker;


    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 店铺id
     */
    private String shopId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 当前方案合计卸货数量
     */
    private BigDecimal sumVal;

}
