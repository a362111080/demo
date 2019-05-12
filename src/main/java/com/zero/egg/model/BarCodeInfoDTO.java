package com.zero.egg.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 二维详细信息DTO
 *
 * @ClassName BarCodeInfoDTO
 * @Author lyming
 * @Date 2019/5/3 3:53
 **/
@Data
public class BarCodeInfoDTO implements Serializable {

    private static final long serialVersionUID = 2619796349552165567L;
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
//    private String code;

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
     * 当前编号
     */
    private String currentCode;

}
