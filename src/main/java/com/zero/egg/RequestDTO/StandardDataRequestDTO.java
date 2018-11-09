package com.zero.egg.RequestDTO;

import lombok.Data;

import java.io.Serializable;

/**
 * 规格方案RequestDTO
 *
 * @ClassName StandardDataRequestDTO
 * @Author lyming
 * @Date 2018/11/9 13:49
 **/
@Data
public class StandardDataRequestDTO implements Serializable {

    private static final long serialVersionUID = 3598140603310947570L;

    /**
     * 主键id
     */
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
