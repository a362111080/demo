package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName BlankBillGoodsDetail
 * @Author lyming
 * @Date 2019/5/18 15:33
 **/
@Data
public class BlankBillGoodsDetail implements Serializable {

    private static final long serialVersionUID = 8974798696362183715L;

    /**
     * 方案细节(规格)id
     */
    private String specificationId;
    /**
     * 标记方式
     */
    private String marker;

    /**
     * 计重方式(1:去皮 2:包)
     */
    private int mode;

    /**
     * 去皮时,实重总和
     */
    private BigDecimal totalWeight;

    /**
     * 数量
     */
    private Long count;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 小计
     */
    private BigDecimal subtotal;

}
