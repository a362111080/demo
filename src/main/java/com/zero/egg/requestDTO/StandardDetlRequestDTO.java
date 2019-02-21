package com.zero.egg.requestDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName StandardDetlRequestDTO
 * @Author lyming
 * @Date 2018/11/9 14:41
 **/
@Data
public class StandardDetlRequestDTO implements Serializable {


    private static final long serialVersionUID = -378414257627802766L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 方案id
     */
    private String programId;

    /**
     * 判定最小称重
     */
    private BigDecimal weightMin;

    /**
     * 判定最大称重
     */
    private BigDecimal weightMax;

    /**
     * 标记
     */
    private String marker;

    /**
     * 计重方式(1:去皮 2:包)
     */
    private Integer mode;

    /**
     * 数值（去皮对应的数值）
     */
    private Integer numerical;

    /**
     * 是否预警(0:否,1:是)
     */
    private Integer warn;
    /**
     * 所属店铺id
     */
    private String shopId;

    /**
     * 店铺所属企业id
     */
    private String companyId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
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
     * 删除标识 0:未删除(默认) 1:已删除
     */
    private Integer dr = 0;

    /**
     * 前端传来的id数组(批量操作)
     */
    private List<String> ids;
}
