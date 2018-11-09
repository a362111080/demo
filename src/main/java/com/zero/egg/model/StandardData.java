package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 规格方案
 *
 * @ClassName StandardData
 * @Author lyming
 * @Date 2018/11/9 13:43
 **/
@Data
@TableName(value = "SMS_Standard_Data")
public class StandardData {
    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 方案主表编码
     */
    private String strStandCode;

    /**
     * 方案名称
     */
    private String strStandName;

    /**
     * 品种编码
     */
    private String strEggTypeCode;

    /**
     * 状态 0:停用 1:启用(默认)
     */
    private Integer LngState = 1;
}
