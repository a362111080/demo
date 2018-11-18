package com.zero.egg.requestDTO;

import lombok.Data;

import java.io.Serializable;
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
     * 计重方式(1:去皮 2:包)
     */
    private Integer strModeType;

    /**
     * 数值（去皮对应的数值）
     */
    private Integer strStandNum;

    /**
     * 是否预警
     */
    private Integer lngWarNing;

    /**
     * 状态 0:停用 1:启用(默认)
     */
    private Integer LngState = 1;

    /**
     * 前端传来的id数组(批量操作)
     */
    private List<String> ids;
}
