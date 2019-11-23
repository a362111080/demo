package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class BarCodeRequestDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = 9013224672043236709L;

    /**
     * 条码主键
     */
    private String id;

    /**
     * 企业主键id
     */
    private String companyId;

    /**
     * 店铺主键id
     */
    private String shopId;

    /**
     * 编码
     */
    private String code;

    /**
     * 供应商
     */
    private String supplierId;


    /**
     * 供应商名
     */
    private String supplierName;

    /**
     * 品种
     */
    private String categoryId;

    /**
     * 二维码图片地址
     */
    private String matrixAddr;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 类别创建时间
     */
    private Date createtime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date modifytime;

    /**
     * 前端传来的id数组(批量操作)
     */
    private List<String> ids;
}
