package com.zero.egg.controller;

import com.zero.egg.tool.PageDTO;
import lombok.Data;

/**
 * 二维码列表查询
 *
 * @ClassName BarCodeListRequestDTO
 * @Author lyming
 * @Date 2019/5/4 11:38
 **/
@Data
public class BarCodeListRequestDTO extends PageDTO {

    /**
     * 供应商名字关键字(模糊查询)
     */
    private String supplierName;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 店铺id
     */
    private String shopId;

}
