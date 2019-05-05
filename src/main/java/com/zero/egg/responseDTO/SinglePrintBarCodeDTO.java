package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SinglePrintBarCodeDTO
 * @Author lyming
 * @Date 2019/5/5 11:43 PM
 **/
@Data
public class SinglePrintBarCodeDTO implements Serializable {

    /**
     * 二维码相对地址
     */
    private String matrixAddr;

    /**
     * 商品编码
     */
    private String currentCode;

    /**
     * 当前店铺名
     */
    private String shopName;

    /**
     * 品种名
     */
    private String categoryName;

}
