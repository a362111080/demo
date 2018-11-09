package com.zero.egg.responseDTO;

import com.zero.egg.model.StandardDetl;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 方案及其细节列表ResponseDTO
 *
 * @ClassName StandardDataListResponseDTO
 * @Author lyming
 * @Date 2018/11/10 4:33
 **/
@Data
public class StandardDataListResponseDTO implements Serializable {

    private static final long serialVersionUID = 451479826913900341L;

    /**
     * 方案id
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

    /**
     * 方案细节列表
     */
    private List<StandardDetl> standardDetlList;

}
