package com.zero.egg.model;

import lombok.Data;

/**
 * 二维详细信息DTO
 *
 * @ClassName BarCodeInfoDTO
 * @Author lyming
 * @Date 2019/5/3 3:53
 **/
@Data
public class BarCodeInfoDTO {

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商识别码
     */
    private String code;

    /**
     * 类别id
     */
    private String categoryId;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 店铺id
     */
    private String shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 打印数量
     */
    private Integer printNum;

    /**
     * 当前编号
     */
    private String currentCode;

}
