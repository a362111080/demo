package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 规格方案细节
 *
 * @ClassName StandardDetl
 * @Author lyming
 * @Date 2018/11/9 14:32
 **/
@TableName(value = "SMS_Standard_Detl")
@Data
public class StandardDetl {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
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
}
