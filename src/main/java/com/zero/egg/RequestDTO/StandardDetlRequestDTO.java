package com.zero.egg.RequestDTO;

import java.io.Serializable;

/**
 * @ClassName StandardDetlRequestDTO
 * @Author lyming
 * @Date 2018/11/9 14:41
 **/
public class StandardDetlRequestDTO implements Serializable {


    private static final long serialVersionUID = -378414257627802766L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 方案明细编码
     */
    private String strStandDetlCode;

    /**
     * 方案主编码
     */
    private String strStandCode;

    /**
     * 判定最小称重
     */
    private Integer strWeightMin;

    /**
     * 判定最大称重
     */
    private Integer strWeightMax;

    /**
     * 反馈标示
     */
    private String strReturnSign;

    /**
     * 计重方式
     */
    private String strModeType;

    /**
     * 数值（去皮对应的数值）
     */
    private Integer strStandNum;

    /**
     * 是否预警
     */
    private Integer LngWarNing;

    /**
     * 状态 0:停用 1:启用(默认)
     */
    private Integer LngState = 1;
}
